package com.example.logifitappp.core.utils

object StringUtils {
    fun hexStringToByteArray(string: String): ByteArray {
        val len = string.length
        val data = ByteArray(len / 2)

        for (i in 0 until len step 2) {
            data[i / 2] = ((Character.digit(string[i], 16) shl 4) + Character.digit(string[i + 1], 16)).toByte()
        }

        return data
    }
}