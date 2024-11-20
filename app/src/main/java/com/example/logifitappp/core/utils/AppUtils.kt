package com.example.logifitappp.core.utils

object AppUtils {
    fun hexStringToByteArray(text: String): ByteArray {
        val len = text.length
        val data = ByteArray(len / 2)

        for (i in 0 ..< len step 2) {
            data[i / 2] = ((text[i].digitToIntOrNull(16)!! shl 4) + text[i + 1].digitToIntOrNull(16)!!).toByte()
        }

        return data
    }
}