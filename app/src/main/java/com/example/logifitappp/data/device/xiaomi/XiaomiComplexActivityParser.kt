package com.example.logifitappp.data.device.xiaomi

import org.slf4j.LoggerFactory
import java.nio.ByteBuffer

class XiaomiComplexActivityParser(
    private val header: ByteArray,
    private val buf: ByteBuffer
) {
    private var currentGroup = -1
    private var currentGroupBits = 0
    private var currentVal = 0

    fun reset() {
        currentGroup = -1
        currentGroupBits = 0
        currentVal = 0
    }

    fun nextGroup(nBits: Int): Boolean {
        currentGroup++
        if (currentGroup >= header.size * 2) {
            LOG.error("Header too small for group {}", currentGroup)
            consume(nBits)
            return false
        }
        if (getCurrentNibble() and 8 == 0) {
            return false
        }
        currentGroupBits = nBits
        currentVal = consume(nBits)
        return getCurrentNibble() and 8 != 0
    }

    private fun consume(nBits: Int): Int = when (nBits) {
        8 -> buf.get().toInt() and 0xff
        16 -> buf.short.toInt() and 0xffff
        32 -> buf.int
        else -> throw IllegalArgumentException("Unsupported number of bits $nBits")
    }

    private fun getCurrentNibble(): Int {
        val headerByte = currentGroup / 2
        return if (currentGroup % 2 == 0) {
            (header[headerByte].toInt() and 0xf0) shr 4
        } else {
            header[headerByte].toInt() and 0x0f
        }
    }

    fun hasFirst(): Boolean = isValid(0)
    fun hasSecond(): Boolean = isValid(1)
    fun hasThird(): Boolean = isValid(2)

    fun isValid(idx: Int): Boolean {
        require(idx in 0..2) { "Invalid idx $idx" }
        return getCurrentNibble() and (1 shl (2 - idx)) != 0
    }

    fun get(idx: Int, nBits: Int): Int {
        val shift = currentGroupBits - idx - nBits
        return (currentVal and (((1 shl nBits) - 1) shl shift)) ushr shift
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(XiaomiComplexActivityParser::class.java)
    }
}