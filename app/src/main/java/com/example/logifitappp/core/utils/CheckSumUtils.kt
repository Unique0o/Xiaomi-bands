package com.example.logifitappp.core.utils

import java.util.zip.CRC32

object CheckSumUtils {
    fun getCRC32(seq: ByteArray, offset: Int, length: Int): Int {
        val crc = CRC32()
        crc.update(seq, offset, length)
        return crc.value.toInt()
    }
}