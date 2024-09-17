package com.example.logifitappp.core.wearebles.xiaomi.parsers

import java.nio.ByteBuffer

class XiaomiComplexActivityParser(private val header: ByteArray, private val buffer: ByteBuffer) {
    private var currentGroup = -1
    private var currentGroupBits = 0
    private var currentVal = 0

    private fun consume(bits: Int) = when (bits) {
        8 -> buffer.get().toInt() and 0xff
        16 -> buffer.getShort().toInt() and 0xffff
        32 -> buffer.getInt()
        else -> throw IllegalArgumentException("Unsupported number of bits $bits")
    }

    fun get(idx: Int, bits: Int): Int {
        val shift = currentGroupBits - idx - bits
        return (currentVal and (((1 shl bits) - 1) shl shift)) ushr shift
    }

    private fun getCurrentNibble(): Int {
        val headerByte = currentGroup / 2

        return if (currentGroup % 2 == 0) (header[headerByte].toInt() and 0xf0) shr 4 else header[headerByte].toInt() and 0xf0
    }

    fun hasFirst() = isValid(0)

    fun hasSecond() = isValid(1)

    fun hasThrid() = isValid(2)

    private fun isValid(idx: Int): Boolean {
        if (idx < 0 || idx > 2) throw IllegalArgumentException("Invalid idx $idx")

        return (getCurrentNibble() and (1 shl (2 - idx))) != 0
    }

    fun nextGroup(bits: Int): Boolean {
        currentGroup++

        if (currentGroup >= header.size * 2) {
            println("Header too small for group $currentGroup")
            consume(bits)

            return false
        }

        if ((getCurrentNibble() and 8) == 0) return false

        currentGroupBits = bits
        currentVal = consume(bits)

        return (getCurrentNibble() and 8) != 0
    }

    fun reset() {
        currentGroup = -1
        currentGroupBits = 0
        currentVal = 0
    }
}