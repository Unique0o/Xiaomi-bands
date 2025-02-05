package com.example.logifitappp.core.wearebles.huami.zeppos.services

import androidx.annotation.RequiresPermission
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.events.AbstractWearableEvent
import com.example.logifitappp.core.wearebles.huami.zeppos.ZeppOsSupport

abstract class AbstractZeppOsService(protected val support: ZeppOsSupport, private var encrypted: Boolean) {
    protected fun evaluateWearableEvent(event: AbstractWearableEvent) {
        support.evaluateWearableEvent(event)
    }

    fun initialize(builder: TransactionBuilder) {

    }

    fun setEncrypted(encrypted: Boolean) {
        if (encrypted != this.encrypted) println("Replacing encrypted flag for ${javaClass.simpleName}, ${this.encrypted} -> $encrypted")

        this.encrypted = encrypted
    }

    protected fun write(builder: TransactionBuilder, byte: Byte) {
        write(builder, byteArrayOf(byte))
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    protected fun write(taskName: String, payload: ByteArray) {
        support.writeToChunked2021(taskName, getEndpoint(), payload, encrypted)
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