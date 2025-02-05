package com.example.logifitappp.core.wearebles.xiaomi

import com.example.logifitappp.core.wearebles.xiaomi.services.XiaomiAuthService
import com.example.logifitappp.enums.XiaomiChannelTypeEnum
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.atomic.AtomicInteger

class XiaomiSppPacketV1 {
    var channel = XiaomiChannelTypeEnum.UNKNOWN
        private set

    private var dataType = 0
    private var frameSerial = 0
    private var opCode = 0
    private var rawChannel = 0

    private var flag = false
    private var needsResponse = false

    private var payload: ByteArray? = null

    fun encode(authService: XiaomiAuthService?, encryptionCounter: AtomicInteger?): ByteArray {
        val payload = this.payload ?: byteArrayOf()

        val bytes = when {
            dataType == DATA_TYPE_ENCRYPTED && channel == XiaomiChannelTypeEnum.PROTOBUF_COMMAND -> run protobuf@{
                val packetCounter = encryptionCounter!!.incrementAndGet()
                val buffer = authService!!.encrypt(payload, packetCounter)

                return@protobuf ByteBuffer.allocate(buffer.size + 2)
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .putShort(packetCounter.toShort())
                    .put(buffer)
                    .array()
            }

            dataType == DATA_TYPE_ENCRYPTED -> authService!!.encrypt(payload, 0)
            else -> payload
        }

        return ByteBuffer.allocate(11 + bytes.size)
            .order(ByteOrder.LITTLE_ENDIAN)
            .put(PACKET_PREAMBLE)
            .put((getRawChannel(channel, true) and 0xf).toByte())
            .put(((if (flag) 0x80 else 0) or (if (needsResponse) 0x40 else 0)).toByte())
            .putShort((bytes.size + 3).toShort())
            .put((opCode and 0xff).toByte())
            .put((frameSerial and 0xff).toByte())
            .put((dataType and 0xff).toByte())
            .put(bytes)
            .put(PACKET_EPILOGUE)
            .array()
    }

    fun getDecryptedPayload(authService: XiaomiAuthService?): ByteArray? {
        if (payload == null) {
            println("getDecryptedPayload(): payload is null")
            return null
        }

        if (authService == null) {
            println("getDecryptedPayload(): authService is null")
            return payload
        }

        if (!authService.isEncryptionInitialized() && dataType == DATA_TYPE_ENCRYPTED) {
            println("getDecryptedPayload(): authService is not ready to decrypt")
            return payload
        }

        if (dataType == DATA_TYPE_ENCRYPTED) return authService.decrypt(payload!!)

        return payload
    }

    class Builder {
        private var payload = ByteArray(0)
        private var flag = true
        private var needsResponse = false
        private var channel: XiaomiChannelTypeEnum = XiaomiChannelTypeEnum.UNKNOWN
        private var opCode = -1
        private var frameSerial = -1
        private var dataType = -1

        fun build(): XiaomiSppPacketV1 {
            val result = XiaomiSppPacketV1()

            result.channel = channel
            result.flag = flag
            result.needsResponse = needsResponse
            result.opCode = opCode
            result.frameSerial = frameSerial
            result.dataType = dataType
            result.payload = payload
            result.rawChannel = getRawChannel(channel, true)

            return result
        }

        fun channel(channel: XiaomiChannelTypeEnum): Builder {
            this.channel = channel
            return this
        }

        fun flag(flag: Boolean): Builder {
            this.flag = flag
            return this
        }

        fun needsResponse(needsResponse: Boolean): Builder {
            this.needsResponse = needsResponse
            return this
        }

        fun opCode(opCode: Int): Builder {
            this.opCode = opCode
            return this
        }

        fun frameSerial(frameSerial: Int): Builder {
            this.frameSerial = frameSerial
            return this
        }

        fun dataType(dataType: Int): Builder {
            this.dataType = dataType
            return this
        }

        fun payload(payload: ByteArray): Builder {
            this.payload = payload
            return this
        }
    }

    companion object {
        const val CHANNEL_FITNESS = 3
        const val CHANNEL_MASS = 5
        const val CHANNEL_PROTO_RX = 1
        const val CHANNEL_PROTO_TX = 2
        const val CHANNEL_VERSION = 0

        const val DATA_TYPE_AUTH = 2
        const val DATA_TYPE_ENCRYPTED = 1
        const val DATA_TYPE_PLAIN = 0

        const val OPCODE_READ = 0
        const val OPCODE_SEND = 2

        val PACKET_EPILOGUE = byteArrayOf(0xef.toByte())
        val PACKET_PREAMBLE = byteArrayOf(0xba.toByte(), 0xdc.toByte(), 0xfe.toByte())

        fun decode(packet: ByteArray): XiaomiSppPacketV1? {
            if (packet.size < 11) {
                println("Cannot decode incomplete packet")
                return null
            }

            val buffer = ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN)
            val preamble = ByteArray(PACKET_PREAMBLE.size)
            buffer.get(preamble)

            if (!PACKET_PREAMBLE.contentEquals(preamble)) {
                println("Expected preamble ${PACKET_PREAMBLE.size} does not match found preamble ${preamble.contentToString()}")
                return null
            }

            var channel = buffer.get()

            if ((channel.toInt() and 0xf0) != 0) channel = 0x0f

            val flags = buffer.get()
            val flag = (flags.toInt() and 0x80) != 0
            val needsResponse = (flags.toInt() and 0x40) != 0
            val payloadLength = (buffer.getShort().toInt() and 0xffff) - 3

            if (payloadLength + 11 > packet.size) {
                println("Packet incomplete (expected length: ${payloadLength + 11}, actual length: ${packet.size})")
                return null
            }

            val opCode = buffer.get().toInt() and 0xff
            val frameSerial = buffer.get().toInt() and 0xff
            val dataType = buffer.get().toInt() and 0xff

            val payload = ByteArray(payloadLength)
            buffer.get(payload)

            val epilogue = ByteArray(PACKET_EPILOGUE.size)
            buffer.get(epilogue)

            if (!PACKET_EPILOGUE.contentEquals(epilogue)) {
                println("Expected epilogue ${PACKET_EPILOGUE.size} does not match actual epilogue ${epilogue.contentToString()}")
                return null
            }

            return XiaomiSppPacketV1().apply {
                this.rawChannel = channel.toInt()
                this.channel = getChannel(channel)
                this.flag = flag
                this.needsResponse = needsResponse
                this.opCode = opCode
                this. frameSerial = frameSerial
                this.dataType = dataType
                this.payload = payload
            }
        }

        private fun getChannel(rawChannel: Byte) = when (rawChannel.toInt() and 0xff) {
            CHANNEL_PROTO_RX, CHANNEL_PROTO_TX -> XiaomiChannelTypeEnum.PROTOBUF_COMMAND
            CHANNEL_FITNESS -> XiaomiChannelTypeEnum.ACTIVITY
            CHANNEL_MASS -> XiaomiChannelTypeEnum.DATA
            CHANNEL_VERSION -> XiaomiChannelTypeEnum.VERSION
            else -> XiaomiChannelTypeEnum.UNKNOWN
        }

        fun getDataTypeForChannel(channel: XiaomiChannelTypeEnum?) = when (channel) {
            XiaomiChannelTypeEnum.AUTHENTICATION -> DATA_TYPE_AUTH

            XiaomiChannelTypeEnum.PROTOBUF_COMMAND,
            XiaomiChannelTypeEnum.VERSION,
            XiaomiChannelTypeEnum.DATA -> DATA_TYPE_ENCRYPTED

            else -> DATA_TYPE_PLAIN
        }

        private fun getRawChannel(channel: XiaomiChannelTypeEnum?, tx: Boolean) = when (channel) {
            XiaomiChannelTypeEnum.VERSION -> CHANNEL_VERSION
            XiaomiChannelTypeEnum.ACTIVITY -> CHANNEL_FITNESS
            XiaomiChannelTypeEnum.DATA -> CHANNEL_MASS

            XiaomiChannelTypeEnum.AUTHENTICATION,
            XiaomiChannelTypeEnum.PROTOBUF_COMMAND -> if (tx) CHANNEL_PROTO_TX else CHANNEL_PROTO_RX

            else -> -1
        }

        fun newBuilder() = Builder()
    }
}