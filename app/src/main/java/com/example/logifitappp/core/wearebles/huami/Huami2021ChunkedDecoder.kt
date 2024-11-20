package com.example.logifitappp.core.wearebles.huami

import com.example.logifitappp.core.utils.CryptoUtils
import org.apache.commons.lang3.ArrayUtils
import java.nio.ByteBuffer

class Huami2021ChunkedDecoder(private var huami2021Handler: Huami2021Handler, private val force2021Protocol: Boolean) {
    private var currentHandle: Byte? = null
    private var currentLength = 0
    private var currentType = 0
    private var reassemblyBuffer: ByteBuffer? = null
    private var sharedSessionKey: ByteArray? = null

    var lastCount = 0.toByte()
        private set

    var lastHandle = 0.toByte()
        private set

    fun decode(payload: ByteArray): Boolean {
        var i = 0

        if (payload[i++] != 0x03.toByte()) return false.also { println("Ignoring non-chunked payload") }

        val flags = payload[i++].toInt()
        val encrypted = (flags and 0x08) == 0x08
        val firstChunk = (flags and 0x01) == 0x01
        val lastChunk = (flags and 0x02) == 0x02
        val needsAck = (flags and 0x04) == 0x04

        if (force2021Protocol) i++

        val handle = payload[i++]

        if (currentHandle != null && currentHandle != handle) {
            return false.also { println("ignoring handle $handle, expected $currentHandle") }
        }

        lastHandle = handle
        lastCount = payload[i++]

        if (firstChunk) {
            var fullLength = (payload[i++].toInt() and 0xff) or ((payload[i++].toInt() and 0xff) shl 8) or ((payload[i++].toInt() and 0xff) shl 16) or ((payload[i++].toInt() and 0xff) shl 24)
            currentLength = fullLength

            if (encrypted) {
                var encryptedLength = fullLength + 8
                val overflow = encryptedLength % 16

                if (overflow > 0) encryptedLength += (16 - overflow)

                fullLength = encryptedLength
            }

            reassemblyBuffer = ByteBuffer.allocate(fullLength)
            currentType = (payload[i++].toInt() and 0xff) or ((payload[i++].toInt() and 0xff) shl 8)
            currentHandle = handle
        }

        reassemblyBuffer?.put(payload, i, payload.size - i)

        if (lastChunk) {
            reassemblyBuffer?.let {
                var buffer = it.array()

                if (encrypted) {
                    if (sharedSessionKey == null) {
                        currentHandle = null
                        currentType = 0

                        return false.also { println("Got encrypted message, but there's no shared session key") }
                    }

                    val messageKey = ByteArray(16)

                    for (j in 0 ..< 16) messageKey[j] = (sharedSessionKey!![j].toInt() xor handle.toInt()).toByte()

                    try {
                        buffer = CryptoUtils.decryptAES(buffer, messageKey)
                        buffer = ArrayUtils.subarray(buffer, 0, currentLength)
                    } catch (e: Exception) {
                        currentHandle = null
                        currentType = 0

                        return false.also { println("error decrypting $e") }
                    }
                }

                try {
                    huami2021Handler.handle2021Payload(currentType.toShort(), buffer)
                } catch (e: Exception) {
                    println("Failed to handle payload $e")
                }

                currentHandle = null
                currentType = 0
            }
        }

        return needsAck
    }

    fun setEncryptionParameters(sharedSessionKey: ByteArray) {
        this.sharedSessionKey = sharedSessionKey
    }

    fun setHuami2021Handler(huami2021Handler: Huami2021Handler) {
        this.huami2021Handler = huami2021Handler
    }
}