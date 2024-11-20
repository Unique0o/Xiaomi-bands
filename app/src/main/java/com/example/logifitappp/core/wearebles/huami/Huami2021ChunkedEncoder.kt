package com.example.logifitappp.core.wearebles.huami

import android.bluetooth.BluetoothGattCharacteristic
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.utils.CheckSumUtils
import com.example.logifitappp.core.utils.CryptoUtils
import kotlin.math.min

class Huami2021ChunkedEncoder(
    private val characteristicChunked2021Write: BluetoothGattCharacteristic,
    private val force2021Protocol: Boolean,
    private var mtu: Int
) {
    private var encryptedSequenceNr = 0
    private var sharedSessionKey: ByteArray? = null
    private var writeHandle = 0.toByte()

    fun setEncryptionParameters(encryptedSequenceNr: Int, sharedSessionKey: ByteArray) {
        this.encryptedSequenceNr = encryptedSequenceNr
        this.sharedSessionKey = sharedSessionKey
    }

    fun setMtu(mtu: Int) {
        this.mtu = mtu
    }

    fun write(builder: TransactionBuilder, type: Short, payload: ByteArray, extendedFlags: Boolean, encrypt: Boolean) {
        if (encrypt && sharedSessionKey == null) {
            println("Can't encrypt without the shared session key")
            return
        }

        var data = payload

        writeHandle++

        var remaining = data.size
        val length = data.size
        var count = 0.toByte()
        var headerSize = 10

        if (extendedFlags) headerSize++

        if (extendedFlags && encrypt) {
            val messageKey = ByteArray(16)

            for (i in 0 ..< 16) messageKey[i] = (sharedSessionKey!![i].toInt() xor writeHandle.toInt()).toByte()

            var encryptedLength = length + 8
            val overflow = encryptedLength % 16

            if (overflow > 0) encryptedLength += (16 - overflow)

            val encryptablePayload = ByteArray(encryptedLength)
            System.arraycopy(data, 0, encryptablePayload, 0, length)
            encryptablePayload[length] = (encryptedSequenceNr and 0xff).toByte()
            encryptablePayload[length + 1] = ((encryptedSequenceNr shr 8) and 0xff).toByte()
            encryptablePayload[length + 2] = ((encryptedSequenceNr shr 16) and 0xff).toByte()
            encryptablePayload[length + 3] = ((encryptedSequenceNr shr 24) and 0xff).toByte()
            encryptedSequenceNr++

            val checksum = CheckSumUtils.getCRC32(encryptablePayload, 0, length + 4)
            encryptablePayload[length + 4] = (checksum and 0xff).toByte()
            encryptablePayload[length + 5] = ((checksum shr 8) and 0xff).toByte()
            encryptablePayload[length + 6] = ((checksum shr 16) and 0xff).toByte()
            encryptablePayload[length + 7] = ((checksum shr 24) and 0xff).toByte()
            remaining = encryptedLength

            try {
                data = CryptoUtils.encryptAES(encryptablePayload, messageKey)
            } catch (e: Exception) {
                println("error while encrypting: $e")
                return
            }
        }

        while (remaining > 0) {
            val maxChunkLength = mtu - 3 - headerSize
            val copyBytes = min(remaining, maxChunkLength)
            val chunk = ByteArray(copyBytes + headerSize)

            var flags = 0.toByte()

            if (encrypt) flags = (flags.toInt() or 0x08).toByte()

            if (count == 0.toByte()) {
                flags = (flags.toInt() or 0x01).toByte()
                var i = 4

                if (extendedFlags) i++

                chunk[i++] = (length and 0xff).toByte()
                chunk[i++] = ((length shr 8) and 0xff).toByte()
                chunk[i++] = ((length shr 16) and 0xff).toByte()
                chunk[i++] = ((length shr 24) and 0xff).toByte()
                chunk[i++] = (type.toInt() and 0xff).toByte()
                chunk[i] = ((type.toInt() shr 8) and 0xff).toByte()
            }

            if (remaining <= maxChunkLength) flags = (flags.toInt() or 0x06).toByte()

            chunk[0] = 0x03
            chunk[1] = flags

            if (extendedFlags) {
                chunk[2] = 0
                chunk[3] = writeHandle
                chunk[4] = count
            } else {
                chunk[2] = writeHandle
                chunk[3] = count
            }

            System.arraycopy(data, data.size - remaining, chunk, headerSize, copyBytes)
            builder.write(characteristicChunked2021Write, chunk)

            remaining -= copyBytes
            headerSize = 4

            if (extendedFlags) headerSize++

            count++
        }
    }
}