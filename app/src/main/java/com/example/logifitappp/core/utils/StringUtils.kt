package com.example.logifitappp.core.utils

import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.StandardCharsets
import kotlin.math.min

object StringUtils {
    fun getFirstOf(first: String?, second: String?): String {
        if (!first.isNullOrEmpty()) return first

        if (second != null) return second

        return ""
    }

    fun hexStringToByteArray(string: String): ByteArray {
        val len = string.length
        val data = ByteArray(len / 2)

        for (i in 0 until len step 2) {
            data[i / 2] = ((Character.digit(string[i], 16) shl 4) + Character.digit(string[i + 1], 16)).toByte()
        }

        return data
    }

    fun truncate(s: String?, maxLength: Int): String {
        if (s == null) return ""

        val length = min(s.length, maxLength)

        if (length < 0) return ""

        return s.substring(0, length)
    }

    fun utf8ByteLength(string: String?, length: Int): Int {
        if (string == null) return 0

        val outBuf = ByteBuffer.allocate(length)
        val inBuf = CharBuffer.wrap(string.toCharArray())
        StandardCharsets.UTF_8.newEncoder().encode(inBuf, outBuf, true)

        return outBuf.position()
    }
}