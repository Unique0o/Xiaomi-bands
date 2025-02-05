package com.example.logifitappp.core.wearebles.xiaomi

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.example.logifitappp.core.builders.bbr.TransactionBuilder
import com.example.logifitappp.core.builders.bbr.actions.PlainAction
import com.example.logifitappp.core.builders.bbr.actions.SetWearableStateAction
import com.example.logifitappp.core.wearebles.AbstractBbrWearableSupport
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.xiaomi.services.XiaomiAuthService
import com.example.logifitappp.enums.XiaomiChannelTypeEnum
import nodomain.freeyourgadget.gadgetbridge.proto.xiaomi.XiaomiProto
import java.io.ByteArrayOutputStream
import java.io.IOException

class XiaomiSppConnectionSupport(private val xiaomiSupport: XiaomiSupport): XiaomiConnectionSupport() {
    private val channelHandlers = hashMapOf(
        XiaomiChannelTypeEnum.VERSION to XiaomiChannelHandler{ payloadBytes: ByteArray -> this.handleVersionPacket(payloadBytes) },
        XiaomiChannelTypeEnum.PROTOBUF_COMMAND to XiaomiChannelHandler{ payload: ByteArray -> xiaomiSupport.handleCommandBytes(payload) },
        XiaomiChannelTypeEnum.ACTIVITY to XiaomiChannelHandler{ payload: ByteArray -> xiaomiSupport.getHealthService().getActivityFetcher().addChunk(payload) }
    )

    private val buffer = ByteArrayOutputStream()
    private var protocol: AbstractXiaomiSppProtocol = XiaomiSppProtocolV1(this)
    private val versionResponseTimeoutHandler = Handler(Looper.getMainLooper())

    val commsSupport = object: AbstractBbrWearableSupport() {
        override fun dispose() {
            xiaomiSupport.onDisconnect()
            super.dispose()
        }

        override fun getSupportedService() = XiaomiUuids.UUID_SERVICE_SERIAL_PORT_PROFILE

        override fun initializeDevice(builder: TransactionBuilder): TransactionBuilder {
            if (getWearable().getFirmwareVersion() == null) {
                getWearable().setFirmwareVersion(xiaomiSupport.getCachedFirmwareVersion() ?: "N/A")
            }

            builder.add(SetWearableStateAction(getWearable(), Wearable.State.INITIALIZING, getContext()))
            builder.add(SetWearableStateAction(getWearable(), Wearable.State.AUTHENTICATING, getContext()))

            builder.write(
                XiaomiSppPacketV1.newBuilder()
                    .channel(XiaomiChannelTypeEnum.VERSION)
                    .needsResponse(true)
                    .opCode(XiaomiSppPacketV1.OPCODE_READ)
                    .dataType(XiaomiSppPacketV1.DATA_TYPE_PLAIN)
                    .frameSerial(0)
                    .build()
                    .encode(null, null)
            )

            builder.add(object: PlainAction() {
                override fun run(socket: BluetoothSocket?): Boolean {
                    versionResponseTimeoutHandler.postDelayed(VersionTimeoutRunnable(), 5000L)
                    return true
                }
            })

            return builder
        }

        override fun onSocketRead(payload: ByteArray) {
            this@XiaomiSppConnectionSupport.onSocketRead(payload)
        }

        override fun useAutoConnect() = xiaomiSupport.useAutoConnect()
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    override fun connect(): Boolean {
        return commsSupport.connect()
    }

    override fun dispose() {
        commsSupport.dispose()
    }

    fun getAuthService() = xiaomiSupport.getAuthService()

    private fun handleVersionPacket(payload: ByteArray?) {
        versionResponseTimeoutHandler.removeCallbacksAndMessages(null)

        if (payload != null && payload.isNotEmpty()) {
            println("Received SPP protocol version: ${payload.contentToString()}")

            if (payload[0] >= 2) {
                println("handleVersionPacket(): detected protocol version higher than 2, switching protocol")
                protocol = XiaomiSppProtocolV2(this)
            }
        }

        if (protocol.initializeSession()) xiaomiSupport.getAuthService().startEncryptedHandshake()
    }

    fun onPacketReceived(channel: XiaomiChannelTypeEnum, payload: ByteArray?) {
        val handler = channelHandlers[channel]

        if (payload != null) handler?.handle(payload)
    }

    fun onSocketRead(payload: ByteArray) {
        try {
            buffer.write(payload)
        } catch (e: IOException) {
            println("Exception while writing buffer: $e")
        }

        processBuffer()
    }

    private fun processBuffer() {
        var shouldProcess = true

        while (shouldProcess) {
            val bufferState = buffer.toByteArray()
            val parseResult = protocol.processPacket(bufferState)

            println("processBuffer(): protocol.processPacket() returned status ${parseResult.status}")
            var skipBytes: Int

            when (parseResult.status) {
                AbstractXiaomiSppProtocol.ParseResult.Status.INCOMPLETE -> {
                    skipBytes = 0
                    shouldProcess = false
                }

                AbstractXiaomiSppProtocol.ParseResult.Status.COMPLETE -> skipBytes = parseResult.packetSize

                AbstractXiaomiSppProtocol.ParseResult.Status.INVALID -> {
                    skipBytes = protocol.findNextPacketOffset(bufferState)

                    if (skipBytes < 0) skipBytes = bufferState.size
                }
            }

            if (skipBytes > 0) {
                println("processBuffer(): skipping $skipBytes bytes for state ${parseResult.status}")
                skipBuffer(skipBytes)
            }
        }
    }

    override fun sendCommand(taskName: String, command: XiaomiProto.Command) {
        try {
            val builder = commsSupport.createTransactionBuilder("send $taskName")
            sendCommand(builder, command)
            commsSupport.getQueue()?.let { builder.queue(it) }
        } catch (e: Exception) {
            println("Caught unexpected exception while sending command, device may not have been informed! - $e")
        }
    }

    private fun sendCommand(builder: TransactionBuilder, command: XiaomiProto.Command) {
        println("sendCommand(): encoded command for task '${builder.transaction.getTaskName()}': ${command.toByteArray().contentToString()}")

        if (command.type == XiaomiAuthService.COMMAND_TYPE) {
            builder.write(protocol.encodePacket(XiaomiChannelTypeEnum.AUTHENTICATION, command.toByteArray()))
        } else builder.write(protocol.encodePacket(XiaomiChannelTypeEnum.PROTOBUF_COMMAND, command.toByteArray()))
    }

    override fun setContext(wearable: Wearable, adapter: BluetoothAdapter, context: Context) {
        commsSupport.setContext(wearable, adapter, context)
    }

    private fun skipBuffer(newState: Int) {
        var ns = newState
        val bufferState = buffer.toByteArray()
        buffer.reset()

        if (ns < 0) ns = bufferState.size

        if (ns >= bufferState.size) return

        buffer.write(bufferState, ns, bufferState.size - ns)
    }

    inner class VersionTimeoutRunnable: Runnable {
        override fun run() {
            println("SPP protocol version request timed out")
            this@XiaomiSppConnectionSupport.handleVersionPacket(byteArrayOf())
        }
    }
}