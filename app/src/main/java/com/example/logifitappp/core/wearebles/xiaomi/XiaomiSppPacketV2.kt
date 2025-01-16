package com.example.logifitappp.core.wearebles.xiaomi

import com.example.logifitappp.core.wearebles.xiaomi.services.XiaomiAuthService
import com.example.logifitappp.enums.XiaomiChannelTypeEnum
import java.nio.ByteBuffer
import java.nio.ByteOrder

abstract class XiaomiSppPacketV2(val packetType: Int, val sequenceNumber: Int) {
    fun encode(authService: XiaomiAuthService?): ByteArray {
        val payload = getPacketPayloadBytes(authService)

        return ByteBuffer.allocate(8 + payload.size).order(ByteOrder.LITTLE_ENDIAN)
            .put(PACKET_PREAMBLE)
            .put((packetType and 0xf).toByte())
            .put((sequenceNumber and 0xff).toByte())
            .putShort(payload.size.toShort())
            .putShort(calculatePayloadChecksum(payload).toShort())
            .put(payload)
            .array()
    }

    abstract fun getPacketPayloadBytes(authService: XiaomiAuthService?): ByteArray

    abstract class Builder<T: Builder<T>> {
        var packetNumber = -1
        var packetType = PACKET_TYPE_UNKNOWN

        fun setPacketType(packetType: Int): T {
            this.packetType = packetType
            return this as T
        }

        fun setSequenceNumber(packetNumber: Int): T {
            this.packetNumber = packetNumber
            return this as T
        }

        abstract fun build(): XiaomiSppPacketV2
    }

    class AckPacket(builder: Builder): XiaomiSppPacketV2(builder.packetType, builder.packetNumber) {
        override fun getPacketPayloadBytes(authService: XiaomiAuthService?) = byteArrayOf()

        class Builder: XiaomiSppPacketV2.Builder<Builder>() {
            init {
                setPacketType(PACKET_TYPE_ACK)
            }

            override fun build() = AckPacket(this)
        }
    }

    class DataPacket(builder: Builder): XiaomiSppPacketV2(builder.packetType, builder.packetNumber) {
        val channel = builder.channel

        private val opCode = builder.opCode
        private val payload = builder.payload

        override fun getPacketPayloadBytes(authService: XiaomiAuthService?): ByteArray = ByteBuffer.allocate(2 + payload.size)
            .put((getRawChannel(channel).toInt() and 0xf).toByte())
            .put((opCode and 0xff).toByte())
            .put(if (opCode == OPCODE_SEND_ENCRYPTED) authService!!.encryptV2(payload) else payload)
            .array()

        fun getPayloadBytes(authService: XiaomiAuthService): ByteArray {
            if (opCode == OPCODE_SEND_ENCRYPTED) return authService.decryptV2(payload)

            return payload
        }

        private fun getRawChannel(channel: XiaomiChannelTypeEnum) = when (channel) {
            XiaomiChannelTypeEnum.AUTHENTICATION,
            XiaomiChannelTypeEnum.PROTOBUF_COMMAND -> CHANNEL_PROTOBUF

            XiaomiChannelTypeEnum.DATA -> CHANNEL_DATA
            XiaomiChannelTypeEnum.ACTIVITY -> CHANNEL_ACTIVITY
            else -> CHANNEL_UNKNOWN
        }.toByte()

        class Builder: XiaomiSppPacketV2.Builder<Builder>() {
            var channel = XiaomiChannelTypeEnum.UNKNOWN
                private set

            var opCode = OPCODE_UNKNOWN
                private set

            var payload = byteArrayOf()
                private set

            init {
                setPacketType(PACKET_TYPE_DATA)
            }

            override fun build() = DataPacket(this)

            fun setChannel(channel: XiaomiChannelTypeEnum): Builder {
                this.channel = channel
                return this
            }

            fun setOpCode(opCode: Int): Builder {
                this.opCode = opCode
                return this
            }

            fun setPayload(payload: ByteArray): Builder {
                this.payload = payload
                return this
            }
        }

        companion object {
            const val CHANNEL_ACTIVITY = 5
            const val CHANNEL_DATA = 2
            const val CHANNEL_PROTOBUF = 1
            const val CHANNEL_UNKNOWN = -1

            const val OPCODE_SEND_ENCRYPTED = 2
            const val OPCODE_SEND_PLAINTEXT = 1
            const val OPCODE_UNKNOWN = -1

            fun decodePacketPayload(sequenceNumber: Int, payload: ByteArray?): XiaomiSppPacketV2? {
                if (payload == null || payload.size < 2) {
                    println("DataPacket.decodePacketPayload(): not enough bytes to decode data packet payload")
                    return null
                }

                val buffer = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN)
                val rawChannel = buffer.get().toInt() and 0xf
                val opCode = buffer.get().toInt() and 0xff

                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)

                return Builder()
                    .setSequenceNumber(sequenceNumber)
                    .setChannel(getChannelFromRaw(rawChannel))
                    .setOpCode(opCode)
                    .setPayload(bytes)
                    .build()
            }

            private fun getChannelFromRaw(rawChannel: Int) = when (rawChannel) {
                CHANNEL_PROTOBUF -> XiaomiChannelTypeEnum.PROTOBUF_COMMAND
                CHANNEL_ACTIVITY -> XiaomiChannelTypeEnum.ACTIVITY
                CHANNEL_DATA -> XiaomiChannelTypeEnum.DATA
                else -> XiaomiChannelTypeEnum.UNKNOWN
            }

            fun getOpCodeForChannel(channel: XiaomiChannelTypeEnum) = when (channel) {
                XiaomiChannelTypeEnum.AUTHENTICATION,
                XiaomiChannelTypeEnum.DATA -> OPCODE_SEND_PLAINTEXT

                XiaomiChannelTypeEnum.PROTOBUF_COMMAND,
                XiaomiChannelTypeEnum.ACTIVITY -> OPCODE_SEND_ENCRYPTED

                else -> OPCODE_UNKNOWN
            }
        }
    }

    class SessionConfigPacket(builder: Builder): XiaomiSppPacketV2(builder.packetType, builder.packetNumber) {
        val opCode = builder.opCode

        override fun getPacketPayloadBytes(authService: XiaomiAuthService?) = byteArrayOf(
            opCode.toByte(),
            KEY_VERSION.toByte(),
            0x03, 0x00,
            0x01, 0x00, 0x00,
            KEY_MAX_PACKET_SIZE.toByte(),
            0x02, 0x00,
            0x00, 0xfc.toByte(),
            KEY_TX_WIN.toByte(),
            0x02, 0x00,
            0x20, 0x00,
            KEY_SEND_TIMEOUT.toByte(),
            0x02,
            0x10, 0x27
        )

        class Builder: XiaomiSppPacketV2.Builder<Builder>() {
            var opCode = -1
                private set

            init {
                setPacketType(PACKET_TYPE_SESSION_CONFIG)
            }

            override fun build() = SessionConfigPacket(this)

            fun setOpCode(opCode: Int): Builder {
                this.opCode = opCode
                return this
            }
        }

        companion object {
            const val KEY_MAX_PACKET_SIZE = 2
            const val KEY_SEND_TIMEOUT = 4
            const val KEY_TX_WIN = 3
            const val KEY_VERSION = 1

            const val OPCODE_START_SESSION_REQUEST = 1
            const val OPCODE_START_SESSION_RESPONSE = 2
            const val OPCODE_STOP_SESSION_REQUEST = 3
            const val OPCODE_STOP_SESSION_RESPONSE = 4

            const val VALUE_SIZE_MAX_PACKET_SIZE = 2
            const val VALUE_SIZE_SEND_TIMEOUT = 2
            const val VALUE_SIZE_TX_WIN = 2
            const val VALUE_SIZE_VERSION = 3

            fun decodePayloadBytes(sequenceNumber: Int, payload: ByteArray): XiaomiSppPacketV2? {
                val buffer = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN)

                if (buffer.remaining() < 1) {
                    println("SessionConfig.decodePayloadBytes(): at least 1 byte required to decode")
                    return null
                }

                val opCode = buffer.get().toInt() and 0xff

                when (opCode) {
                    OPCODE_START_SESSION_REQUEST, OPCODE_START_SESSION_RESPONSE -> {
                        while (buffer.remaining() >= 3) {
                            val key = buffer.get().toInt() and 0xff
                            val valueSize = buffer.getShort().toInt() and 0xffff

                            if (buffer.remaining() < valueSize) {
                                println("not enough bytes remaining to extract value")
                                break
                            }

                            when (key) {
                                KEY_VERSION -> {
                                    if (valueSize != VALUE_SIZE_VERSION) {
                                        println("expected $VALUE_SIZE_VERSION bytes for version value, got $valueSize")
                                        buffer.get(ByteArray(valueSize))
                                    } else {
                                        val version = ByteArray(valueSize)
                                        buffer.get(version)
                                        println("received SPPv2 version: ${version.contentToString()}")
                                    }
                                }

                                KEY_MAX_PACKET_SIZE -> {
                                    if (valueSize != VALUE_SIZE_MAX_PACKET_SIZE) {
                                        println("expected 2 bytes for maximum packet size, got $valueSize")
                                        buffer.get(ByteArray(valueSize))
                                    } else println("received max packet size: ${buffer.getShort().toInt() and 0xffff}")
                                }

                                KEY_TX_WIN -> {
                                    if (valueSize != VALUE_SIZE_TX_WIN) {
                                        println("expected $VALUE_SIZE_TX_WIN bytes for transmission window, got $valueSize")
                                        buffer.get(ByteArray(valueSize))
                                    } else println("received tx win: ${buffer.getShort().toInt() and 0xffff}")
                                }

                                KEY_SEND_TIMEOUT -> {
                                    if (valueSize != VALUE_SIZE_SEND_TIMEOUT) {
                                        println("expected $VALUE_SIZE_SEND_TIMEOUT bytes for send timeout value, got $valueSize")
                                        buffer.get(ByteArray(valueSize))
                                    } else println("received send timeout: ${buffer.getShort().toInt() and 0xffff}ms")
                                }

                                else -> {
                                    val version = ByteArray(valueSize)
                                    println("received unknown config type $key with byte value ${version.contentToString()}")
                                }
                            }
                        }
                    }

                    OPCODE_STOP_SESSION_REQUEST, OPCODE_STOP_SESSION_RESPONSE -> {}
                    else -> println("SessionConfigPacket#decode(): unknown opcode $opCode")
                }

                return Builder()
                    .setSequenceNumber(sequenceNumber)
                    .setOpCode(opCode)
                    .build()
            }
        }
    }

    companion object {
        const val PACKET_TYPE_ACK = 1
        const val PACKET_TYPE_DATA = 3
        const val PACKET_TYPE_SESSION_CONFIG = 2
        const val PACKET_TYPE_UNKNOWN = -1

        val PACKET_PREAMBLE = byteArrayOf(0xa5.toByte(), 0xa5.toByte())

        private fun calculatePayloadChecksum(payload: ByteArray): Int {
            var crc = 0

            for (byte in payload) {
                for (j in 0 until 8) {
                    crc = crc shl 1

                    if ((((crc shr 16) and 1) xor ((byte.toInt() shr j) and 1)) == 1) crc = crc xor 0x8005
                }
            }

            return Integer.reverse(crc) ushr 16
        }

        fun decode(payload: ByteArray): XiaomiSppPacketV2? {
            if (payload.size < 8) {
                println("decode(): at least 8 bytes required, got ${payload.size}")
                return null
            }

            val buffer = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN)
            val preamble = ByteArray(PACKET_PREAMBLE.size)
            buffer.get(preamble)

            if (!PACKET_PREAMBLE.contentEquals(preamble)) {
                println("decode(): packet header mismatch: expected ${PACKET_PREAMBLE.contentToString()}, got ${preamble.contentToString()}")
                return null
            }

            val byte = buffer.get()
            val packetType = byte.toInt() and 0xf
            val sequenceNumber = buffer.get().toInt() and 0xff
            val payloadLength = buffer.getShort().toInt() and 0xffff
            val givenChecksum = buffer.getShort().toInt() and 0xffff

            if (buffer.remaining() < payloadLength) {
                println("decode(): expected at least ${payloadLength + 8} bytes in buffer, got ${payload.size} (missing ${payloadLength - buffer.remaining()} bytes to complete packet)")
                return null
            }

            val bytes = ByteArray(payloadLength)
            buffer.get(bytes)
            val calculatedChecksum = calculatePayloadChecksum(bytes)

            if (calculatedChecksum != givenChecksum) {
                println("decode(): payload checksum mismatch (given $givenChecksum != calculated $calculatedChecksum)")
                return null
            }

            return when (packetType) {
                PACKET_TYPE_SESSION_CONFIG -> SessionConfigPacket.decodePayloadBytes(sequenceNumber, bytes)
                PACKET_TYPE_DATA -> DataPacket.decodePacketPayload(sequenceNumber, bytes)

                PACKET_TYPE_ACK -> AckPacket.Builder()
                    .setSequenceNumber(sequenceNumber)
                    .build()

                else -> null
            }
        }

        fun newDataPacketBuilder() = DataPacket.Builder()
        fun newSessionConfigPacketBuilder() = SessionConfigPacket.Builder()
    }
}