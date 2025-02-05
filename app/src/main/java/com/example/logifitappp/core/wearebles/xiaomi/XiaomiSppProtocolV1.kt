package com.example.logifitappp.core.wearebles.xiaomi

import com.example.logifitappp.enums.XiaomiChannelTypeEnum
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.atomic.AtomicInteger

class XiaomiSppProtocolV1(private val connectionSupport: XiaomiSppConnectionSupport): AbstractXiaomiSppProtocol() {
    private val encryptionCounter = AtomicInteger(0)
    private val frameCounter = AtomicInteger(0)

    override fun encodePacket(channel: XiaomiChannelTypeEnum, chunk: ByteArray): ByteArray {
        return XiaomiSppPacketV1.newBuilder()
            .channel(channel)
            .opCode(XiaomiSppPacketV1.OPCODE_SEND)
            .frameSerial(frameCounter.getAndIncrement())
            .dataType(XiaomiSppPacketV1.getDataTypeForChannel(channel))
            .payload(chunk)
            .build()
            .encode(connectionSupport.getAuthService(), encryptionCounter)
    }

    override fun findNextPacketOffset(buffer: ByteArray): Int {
        for (i in 1 until buffer.size) {
            if (buffer[i] == XiaomiSppPacketV1.PACKET_PREAMBLE[0]) return i
        }

        return -1
    }

    override fun processPacket(buffer: ByteArray): ParseResult {
        if (buffer.size < 11) {
            println("processPacket(): not enough bytes in rx buffer to decode packet header")
            return ParseResult(ParseResult.Status.INCOMPLETE)
        }

        val headerBuffer = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN)
        val preamble = ByteArray(XiaomiSppPacketV1.PACKET_PREAMBLE.size)
        headerBuffer.get(preamble)

        if (!XiaomiSppPacketV1.PACKET_PREAMBLE.contentEquals(preamble)) {
            println("processPacket(): header mismatch, expected ${XiaomiSppPacketV1.PACKET_PREAMBLE.contentToString()}, got ${preamble.contentToString()}")
            return ParseResult(ParseResult.Status.INVALID)
        }

        headerBuffer.getShort()
        val payloadSize = headerBuffer.getShort().toInt() and 0xffff
        val packetSize = payloadSize + 8

        if (buffer.size < packetSize) {
            println("processPacket(): received ${buffer.size}, missing ${packetSize - buffer.size}/$packetSize packet bytes")
            return ParseResult(ParseResult.Status.INCOMPLETE)
        }

        println("processPacket(): all bytes for packet of $packetSize bytes in buffer")

        val receivedPacket = XiaomiSppPacketV1.decode(buffer)

        if (receivedPacket == null) {
            println("processPacket(): decoded packet is null")
            return ParseResult(ParseResult.Status.INVALID)
        }

        println("processPacket(): Packet received: $receivedPacket")
        connectionSupport.onPacketReceived(receivedPacket.channel, receivedPacket.getDecryptedPayload(connectionSupport.getAuthService()))

        return ParseResult(ParseResult.Status.COMPLETE, packetSize)
    }
}