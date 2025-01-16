package com.example.logifitappp.core.wearebles.xiaomi

import com.example.logifitappp.enums.XiaomiChannelTypeEnum
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.atomic.AtomicInteger

class XiaomiSppProtocolV2(private val connectionSupport: XiaomiSppConnectionSupport): AbstractXiaomiSppProtocol() {
    private val packetSequenceCounter = AtomicInteger(0)

    override fun encodePacket(channel: XiaomiChannelTypeEnum, chunk: ByteArray) = XiaomiSppPacketV2.newDataPacketBuilder()
        .setChannel(channel)
        .setSequenceNumber(packetSequenceCounter.getAndIncrement())
        .setOpCode(XiaomiSppPacketV2.DataPacket.getOpCodeForChannel(channel))
        .setPayload(chunk)
        .build()
        .encode(connectionSupport.getAuthService())

    override fun findNextPacketOffset(buffer: ByteArray): Int {
        for (i in 1 until buffer.size) {
            if (buffer[i] == XiaomiSppPacketV2.PACKET_PREAMBLE[0]) return i
        }

        return -1
    }

    override fun initializeSession(): Boolean {
        val builder = connectionSupport.commsSupport.createTransactionBuilder("send session config")
        builder.write(
            XiaomiSppPacketV2.newSessionConfigPacketBuilder()
                .setOpCode(XiaomiSppPacketV2.SessionConfigPacket.OPCODE_START_SESSION_REQUEST)
                .setSequenceNumber(0)
                .build()
                .encode(null)
        )

        return false.also { connectionSupport.commsSupport.getQueue()?.let { builder.queue(it) } }
    }

    override fun processPacket(payload: ByteArray): ParseResult {
        if (payload.size < 8) {
            println("processPacket(): not enough bytes in buffer to process packet (got ${payload.size} of required 8 bytes)")
            return ParseResult(ParseResult.Status.INCOMPLETE)
        }

        val buffer = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN)
        val headerMagic = ByteArray(XiaomiSppPacketV2.PACKET_PREAMBLE.size)
        buffer.get(headerMagic)

        if (!XiaomiSppPacketV2.PACKET_PREAMBLE.contentEquals(headerMagic)) {
            println("processPacket(): invalid header magic (expected ${XiaomiSppPacketV2.PACKET_PREAMBLE.contentToString()}, got ${headerMagic.contentToString()})")
            return ParseResult(ParseResult.Status.INVALID)
        }

        buffer.get()
        buffer.get()
        val packetSize = 8 + (buffer.getShort().toInt() and 0xffff)
        buffer.getShort()

        if (payload.size < packetSize) {
            println("processPacket(): missing ${packetSize - payload.size} bytes (got ${payload.size}/$packetSize bytes)")
            return ParseResult(ParseResult.Status.INCOMPLETE)
        }

        XiaomiSppPacketV2.decode(payload)?.let {
            when (it.packetType) {
                XiaomiSppPacketV2.PACKET_TYPE_SESSION_CONFIG -> {
                    println("Received session config, opcode=${(it as XiaomiSppPacketV2.SessionConfigPacket).opCode}")
                    connectionSupport.getAuthService().startEncryptedHandshake()
                }

                XiaomiSppPacketV2.PACKET_TYPE_DATA -> {
                    val dataPacket = it as XiaomiSppPacketV2.DataPacket

                    try {
                        connectionSupport.onPacketReceived(dataPacket.channel, dataPacket.getPayloadBytes(connectionSupport.getAuthService()))
                    } catch (e: Exception) {
                        println("Exception while handling received packet: $e")
                    }

                    sendAck(it.sequenceNumber)
                }

                XiaomiSppPacketV2.PACKET_TYPE_ACK -> println("receive ack for packet ${it.sequenceNumber}")
                else -> println("Unhandled packet with type ${it.packetType} (decoded type ${it.javaClass.simpleName})")
            }
        }

        return ParseResult(ParseResult.Status.COMPLETE, packetSize)
    }

    private fun sendAck(sequenceNumber: Int) {
        val builder = connectionSupport.commsSupport.createTransactionBuilder("send ack for $sequenceNumber")
        builder.write(
            XiaomiSppPacketV2.AckPacket.Builder()
                .setSequenceNumber(sequenceNumber)
                .build()
                .encode(null)
        )

        connectionSupport.commsSupport.getQueue()?.let { builder.queue(it) }
    }
}