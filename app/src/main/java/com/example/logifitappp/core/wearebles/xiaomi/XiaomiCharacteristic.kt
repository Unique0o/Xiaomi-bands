package com.example.logifitappp.core.wearebles.xiaomi

import android.bluetooth.BluetoothGattCharacteristic
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.utils.BleTypeConversionsUtils
import com.example.logifitappp.core.wearebles.xiaomi.services.XiaomiAuthService
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.LinkedList
import java.util.UUID
import kotlin.math.ceil
import kotlin.math.min

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

    private val receivedChunks = mutableMapOf<Int, ByteArray>()
    private val timeoutHandler = Handler(Looper.getMainLooper())
    private val TIMEOUT_TASK_DELAY = 1000L


    fun dispose() {
        cancelTimeoutTask()
    }
    private fun requestMissingChunks() {
        if (numChunks <= 0) {
            println("Timeout task ran but not expecting any chunks")
            return
        }
        println("Timeout reached while waiting for all chunks from device")

        val missingChunks = (1..numChunks).filter { !receivedChunks.containsKey(it) }
        val reqChunkCount = min(missingChunks.size, (maxWriteSize - 4) / 2)

        if (reqChunkCount < missingChunks.size) {
            println("Missing ${missingChunks.size} chunk(s), only requesting first $reqChunkCount: $missingChunks")
        } else {
            println("Missing ${missingChunks.size} chunk(s): $missingChunks")
        }

        val bb = ByteBuffer.allocate(4 + reqChunkCount * 2).order(ByteOrder.LITTLE_ENDIAN)
        bb.putShort(0) // chunk ID
        bb.put(1) // type CHUNKED_ACK
        bb.put(5) // indicate partially received transmission, followed by missing chunks
        missingChunks.take(reqChunkCount).forEach { bb.putShort(it.toShort()) }

        val tb = support.createTransactionBuilder("send nack with missing chunks $missingChunks")
        tb.write(characteristic, bb.array())
        tb.queue(support.getQueue())
    }

    private fun cancelTimeoutTask() {
        timeoutHandler.removeCallbacksAndMessages(null)
    }

    private fun rescheduleTimeoutTask() {
        cancelTimeoutTask()
        timeoutHandler.postDelayed({ requestMissingChunks() }, TIMEOUT_TASK_DELAY)
    }

    private fun reconstructPayloadFromChunks(): ByteArray {
        val out = ByteArrayOutputStream()
        try {
            for (i in 0 until numChunks) {
                val chunk = receivedChunks[i + 1]
                if (chunk == null) {
                    println("Missing chunk ${i + 1}")
                    return ByteArray(0)
                }
                out.write(chunk)
            }
        } catch (ex: IOException) {
            println("Failed to reconstruct payload: ${ex.message}")
            return ByteArray(0)
        }
        return out.toByteArray()
    }
    fun onCharacteristicChanged(payload: ByteArray) {
        val buffer = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN)
        val chunk = buffer.getShort().toInt()

        if (chunk != 0) {
            if (chunk > numChunks) {
                println("Ignoring chunk $chunk exceeding upper bound $numChunks")
                return
            }

            if (receivedChunks.containsKey(chunk)) {
                println("Already received chunk $chunk")
                return
            }

            val chunkBytes = ByteArray(buffer.limit() - buffer.position())
            buffer.get(chunkBytes)
            println("Got chunk $currentChunk of $numChunks")
            receivedChunks[chunk] = chunkBytes

            if (currentChunk == numChunks) {
                cancelTimeoutTask()
                sendChunkEndAck()
                val payload = reconstructPayloadFromChunks()

                if (payload.isEmpty()) {
                    println("Payload reconstructed from chunks was empty")
                } else if (handler != null) {
                    if (isEncrypted) {
                        handler?.handler(authService!!.decrypt(payload))
                    } else {
                        handler?.handler(payload)
                    }
                } else {
                    println("Channel handler for char $characteristicUuid is null!")
                }
                this.numChunks = 0;
                this.receivedChunks.clear();
            } else {
                rescheduleTimeoutTask()
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

                    numChunks = buffer.short.toInt()
                    receivedChunks.clear()

                    println("Got chunked start request for $numChunks chunks")
                    sendChunkStartAck()
                    rescheduleTimeoutTask()
                    return
                }

                1.toByte() -> {
                    val subtype = buffer.get()
                    val remaining = ByteArray(buffer.remaining())

                    if (buffer.hasRemaining()) {
                        buffer.get(remaining)
                        println("Operation CHUNK_ACK of type $subtype has additional payload: ${remaining.contentToString()}")
                    }

                    when (subtype) {
                        0.toByte(),
                        2.toByte() -> {
                            if (subtype == 0.toByte()) {
                                println("Got chunked ack end")
                                currentPayload?.callback?.onSend()
                            } else {
                                println("Got chunked nack for ${currentPayload?.taskName}")
                                currentPayload?.callback?.onNack()
                            }

                            currentPayload = null
                            sendingChunked = false
                            sendNext(null)
                            return
                        }

                        1.toByte() -> {
                            println("Got chunked ack start")

                            val mBuilder = support.createTransactionBuilder("send chunks for ${currentPayload?.taskName}")
                            val bytes = currentPayload!!.getBytesToSend()
                            val chunkPayloadSize = maxWriteSizeForCurrentMessage - 2

                            for (i in 0 .. bytes.size / chunkPayloadSize) sendChunk(mBuilder, i, chunkPayloadSize)

                            mBuilder.queue(support.getQueue())
                            return
                        }

                        5.toByte() -> {
                            val invalidChunks = ShortArray(remaining.size / 2)

                            if (remaining.size > 0) {
                                val remainingBuffer = ByteBuffer.wrap(remaining).order(ByteOrder.LITTLE_ENDIAN)

                                for (i in 0 .. remaining.size / 2) invalidChunks[i] = remainingBuffer.getShort()

                                println("Got chunk request, requested chunks: ${invalidChunks.contentToString()}")
                                val mBuilder = support.createTransactionBuilder("resend chunks for ${currentPayload?.taskName}")

                                invalidChunks.forEach {
                                    sendChunk(mBuilder, it - 1, maxWriteSizeForCurrentMessage - 2)
                                }
                            } else {
                                println("Got chunk request, no chunk indices requested")

                                if (maxWriteSize != maxWriteSizeForCurrentMessage) {
                                    println("MTU changed while sending message, prepending message to queue and resending")
                                    payloadQueue.addFirst(currentPayload)
                                    currentPayload = null
                                    sendingChunked = false
                                    sendNext(null)
                                    return
                                }
                            }
                        }
                    }

                    println("Unknown chunked ack subtype $subtype for ${currentPayload?.taskName}")
                    return
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
        this.receivedChunks.clear();
        payloadQueue.clear()
        waitingAck = false
        sendingChunked = false
        currentPayload = null
        cancelTimeoutTask();
    }

    private fun sendAck() {
        val builder = support.createTransactionBuilder("send ack")
        builder.write(characteristic, byteArrayOf(0, 0, 3, 0))
        builder.queue(support.getQueue())
    }

    private fun sendChunk(builder: TransactionBuilder, index: Int, chunkPayloadSize: Int) {
        val payload = currentPayload!!.getBytesToSend()
        val startIndex = index * chunkPayloadSize
        val endIndex = ((index + 1) * chunkPayloadSize).coerceAtMost(payload.size)

        println("Sending chunk $index from $startIndex to $endIndex for ${currentPayload!!.taskName}")

        val chunkToSend = ByteArray(2 + endIndex - startIndex)
        BleTypeConversionsUtils.writeUint16(chunkToSend, 0, index + 1)
        System.arraycopy(payload, startIndex, chunkToSend, 2, endIndex - startIndex)

        builder.write(characteristic, chunkToSend)
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

            if (builder == null) mBuilder.queue(support.getQueue())
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

            if (builder == null) mBuilder.queue(support.getQueue())
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