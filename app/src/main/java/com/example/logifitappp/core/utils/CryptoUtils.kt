package com.example.logifitappp.core.utils

import android.annotation.SuppressLint
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object CryptoUtils {
    fun decryptAES(value: ByteArray, secretKey: ByteArray): ByteArray {
        @SuppressLint("GetInstance") val ecipher = Cipher.getInstance("AES/ECB/NoPadding")
        val newKey = SecretKeySpec(secretKey, "AES")
        ecipher.init(Cipher.DECRYPT_MODE, newKey)
        return ecipher.doFinal(value)
    }

    fun encryptAES(value: ByteArray?, secretKey: ByteArray?): ByteArray {
        @SuppressLint("GetInstance") val ecipher = Cipher.getInstance("AES/ECB/NoPadding")
        val newKey = SecretKeySpec(secretKey, "AES")
        ecipher.init(Cipher.ENCRYPT_MODE, newKey)
        return ecipher.doFinal(value)
    }
}