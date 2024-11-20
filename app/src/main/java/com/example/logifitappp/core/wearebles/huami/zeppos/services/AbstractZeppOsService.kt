package com.example.logifitappp.core.wearebles.huami.zeppos.services

import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.wearebles.huami.zeppos.ZeppOsSupport

abstract class AbstractZeppOsService(private val support: ZeppOsSupport, private var encrypted: Boolean) {
    fun initialize(builder: TransactionBuilder) {

    }

    fun setEncrypted(encrypted: Boolean) {
        if (encrypted != this.encrypted) println("Replacing encrypted flag for ${javaClass.simpleName}, ${this.encrypted} -> $encrypted")

        this.encrypted = encrypted
    }

    protected fun write(builder: TransactionBuilder, byte: Byte) {
        write(builder, byteArrayOf(byte))
    }

    protected fun write(builder: TransactionBuilder, payload: ByteArray) {
        support.writeToChunked2021(builder, getEndpoint(), payload, encrypted)
    }

    abstract fun getEndpoint(): Short
    abstract fun handlePayload(payload: ByteArray)

    companion object {
        @JvmStatic
        protected fun booleanFromByte(byte: Byte): Boolean? {
            return when (byte) {
                0x00.toByte() -> false
                0x01.toByte() -> true
                else -> null
            }
        }
    }
}