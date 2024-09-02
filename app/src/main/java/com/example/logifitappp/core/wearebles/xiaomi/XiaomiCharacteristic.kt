package com.example.logifitappp.core.wearebles.xiaomi

import android.bluetooth.BluetoothGattCharacteristic
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.wearebles.xiaomi.services.XiaomiAuthService
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.LinkedList
import java.util.UUID
import kotlin.math.ceil

class XiaomiCharacteristic(
    private val support: XiaomiBleConnectionSupport,
    private val characteristic: BluetoothGattCharacteristic,
    private val authService: XiaomiAuthService?
) {
    val characteristicUuid: UUID get() = characteristic.uuid
    private val chunkBuffer = ByteArrayOutputStream()
    private var currentChunk = 0
    private var currentPayload: Payload? = null
    private var encryptedIndex = 0
    private var handler: XiaomiChannelHandler? = null
    private var incrementNonce = true
    private var isEncrypted = authService != null
    private var maxWriteSize = 244
    private var maxWriteSizeForCurrentMessage = 0
    private var numChunks = 0
    private val payloadQueue = LinkedList<Payload>()
    private var sendingChunked = false
    private var waitingAck = false

    fun onCharacteristicChanged(payload: ByteArray) {
        val buffer = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN)
        val chunk = buffer.getShort().toInt()

        if (chunk != 0) {
            val chunkBytes = ByteArray(buffer.limit() - buffer.position())
            buffer.get(chunkBytes)

            try {
                chunkBuffer.write(chunkBytes)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

            currentChunk++
            println("Got chunk $currentChunk of $numChunks")

            if (currentChunk == numChunks) {
                sendChunkEndAck()
                handler?.handler(if (isEncrypted) authService!!.decrypt(chunkBuffer.toByteArray()) else chunkBuffer.toByteArray())

                currentChunk = 0
                chunkBuffer.reset()
            }
        } else {
            val type = buffer.get()

            when(type) {
                0.toByte() -> {
                    val isMessageEncrypted = buffer.get()
                    val expectedResult = if (isEncrypted) 1.toByte() else 0.toByte()

                    if (isMessageEncrypted != expectedResult) {
                        println("Chunked start request: expected $expectedResult, got $isMessageEncrypted")
                        return
                    }

                    numChunks = buffer.getShort().toInt()
                    currentChunk = 0
                    chunkBuffer.reset()

                    println("Got chunked start request for $numChunks chunks")
                    sendChunkStartAck()
                    return
                }

                1.toByte() -> {

                }

                2.toByte() -> {
                    sendAck()

                    val encryption = buffer.get()
                    val bytes = ByteArray(buffer.limit() - buffer.position())
                    buffer.get(bytes)
                    handler?.handler(if (encryption == 1.toByte()) authService!!.decrypt(bytes) else bytes)

                    return
                }

                3.toByte() -> {
                    val result = buffer.get()

                    if (result == 0.toByte()) {
                        println("Got ack for ${currentPayload?.taskName}")
                        currentPayload?.callback?.onSend()
                    } else {
                        println("Got single cmd NACK ($result) for ${currentPayload?.taskName}")
                        currentPayload?.callback?.onNack()
                    }

                    currentPayload = null
                    waitingAck = false
                    sendNext(null)
                    return
                }
            }

            println("Unhandled command type $type")
        }
    }

    fun reset() {
        numChunks = 0
        currentChunk = 0
        encryptedIndex = 1
        chunkBuffer.reset()
        payloadQueue.clear()
        waitingAck = false
        sendingChunked = false
        currentPayload = null
    }

    private fun sendAck() {
        val builder = support.createTransactionBuilder("send ack")
        builder.write(characteristic, byteArrayOf(0, 0, 3, 0))
        builder.queue(support.getQueue())
    }

    private fun sendChunkEndAck() {
        val builder = support.createTransactionBuilder("send chunked end ack")
        builder.write(characteristic, byteArrayOf(0x00, 0x00, 0x01, 0x00))
        builder.queue(support.getQueue())
    }

    private fun sendChunkStartAck() {
        val builder = support.createTransactionBuilder("send chunked start ack")
        builder.write(characteristic, byteArrayOf(0x00, 0x00, 0x01, 0x01))
        builder.queue(support.getQueue())
    }

    private fun sendNext(builder: TransactionBuilder?) {
        if (waitingAck || sendingChunked) {
            println("Already sending something")
            return
        }

        currentPayload = payloadQueue.poll()

        if (currentPayload == null) {
            println("Nothing to send")
            return
        }

        println("Will send ${currentPayload!!.getBytesToSend().contentToString()}")
        val encrypt = isEncrypted && authService!!.isEncryptionInitialized()

        if (encrypt) {
            currentPayload!!.setBytesToSend(authService!!.encrypt(currentPayload!!.getBytesToSend(), if (incrementNonce) encryptedIndex else 0))
        }

        maxWriteSizeForCurrentMessage = maxWriteSize

        if (shouldWriteChunked(currentPayload!!.getBytesToSend())) {
            if (encrypt && incrementNonce) {
                currentPayload!!.setBytesToSend(
                    ByteBuffer.allocate(2 + currentPayload!!.getBytesToSend().size).order(ByteOrder.LITTLE_ENDIAN)
                        .putShort(encryptedIndex++.toShort())
                        .put(currentPayload!!.getBytesToSend())
                        .array()
                )
            }

            println("Sending ${currentPayload?.taskName} - chunked")
            sendingChunked = true

            val buffer = ByteBuffer.allocate(6).order(ByteOrder.LITTLE_ENDIAN)
            buffer.putShort(0)
            buffer.put(0)
            buffer.put(if (encrypt) 1 else 0)
            buffer.putShort(
                ceil(currentPayload!!.getBytesToSend().size / (maxWriteSizeForCurrentMessage - 2).toDouble())
                    .toInt()
                    .toShort()
            )

            val mBuilder = builder ?: support.createTransactionBuilder("send chunked start for ${currentPayload?.taskName}")
            mBuilder.write(characteristic, buffer.array())

            builder?.queue(support.getQueue())
        } else {
            println("Sending ${currentPayload?.taskName} - single")

            val commandLength = (if (encrypt) 6 else 4) + currentPayload!!.getBytesToSend().size

            val buffer = ByteBuffer.allocate(commandLength).order(ByteOrder.LITTLE_ENDIAN)
            buffer.putShort(0)
            buffer.put(2)
            buffer.put(if (encrypt) 1 else 2)

            if (encrypt) {
                buffer.putShort(if (incrementNonce) encryptedIndex++.toShort() else 0)
            }

            buffer.put(currentPayload!!.getBytesToSend())
            waitingAck = true

            val mBuilder = builder ?: support.createTransactionBuilder("send single command for ${currentPayload?.taskName}")
            mBuilder.write(characteristic, buffer.array())

            builder?.queue(support.getQueue())
        }
    }

    fun setIncrementNonce(incrementNonce: Boolean) {
        this.incrementNonce = incrementNonce
    }

    fun setIsEncrypted(isEncrypted: Boolean) {
        this.isEncrypted = isEncrypted
    }

    fun setHandler(handler: XiaomiChannelHandler) {
        this.handler = handler
    }

    fun setMtu(mtu: Int) {
        maxWriteSize = mtu - 3
    }

    private fun shouldWriteChunked(payload: ByteArray): Boolean {
        if (!isEncrypted) return true

        return payload.size + 6 > maxWriteSizeForCurrentMessage
    }

    fun write(taskName: String, payload: ByteArray, callback: SendCallback?) {
        write(null, Payload(taskName, payload, callback))
    }

    fun write(taskName: String, payload: ByteArray) {
        write(taskName, payload, null)
    }

    private fun write(builder: TransactionBuilder?, payload: Payload) {
        payloadQueue.add(payload)
        sendNext(builder)
    }

    private class Payload(val taskName: String, private val bytes: ByteArray, val callback: SendCallback?) {
        private var bytesToSend: ByteArray? = null

        fun getBytesToSend() = bytesToSend ?: bytes

        fun setBytesToSend(bytesToSend: ByteArray) {
            this.bytesToSend = bytesToSend
        }
    }

    interface SendCallback {
        fun onNack()
        fun onSend()
    }
}