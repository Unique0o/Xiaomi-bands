package com.example.logifitappp.core.wearebles.xiaomi

import com.example.logifitappp.enums.XiaomiChannelTypeEnum

abstract class AbstractXiaomiSppProtocol {
    class ParseResult(val status: Status, val packetSize: Int = 0) {
        enum class Status {
            INCOMPLETE,
            INVALID,
            COMPLETE;
        }
    }

    open fun initializeSession() = true

    abstract fun encodePacket(channel: XiaomiChannelTypeEnum, chunk: ByteArray): ByteArray
    abstract fun findNextPacketOffset(buffer: ByteArray): Int
    abstract fun processPacket(buffer: ByteArray): ParseResult
}