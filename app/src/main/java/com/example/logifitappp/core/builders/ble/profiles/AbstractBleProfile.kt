package com.example.logifitappp.core.builders.ble.profiles

import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.handlers.AbstractBluetoothGattCallbackHandler
import com.example.logifitappp.core.handlers.IntentListenerHandler
import com.example.logifitappp.core.wearebles.AbstractBleWearableSupport
import com.example.logifitappp.core.wearebles.Wearable
import java.io.IOException
import java.util.UUID

abstract class AbstractBleProfile<T: AbstractBleWearableSupport>(private val support: T): AbstractBluetoothGattCallbackHandler() {
    private val listeners = mutableListOf<IntentListenerHandler>()

    fun addListener(listener: IntentListenerHandler?) {
        listener?.let {
            if (!listeners.contains(it)) listeners.add(it)
        }
    }

    open fun enableNotify(builder: TransactionBuilder, enable: Boolean) {
    }

    protected fun getCharacteristic(uuid: UUID) = support.getCharacteristic(uuid)


    fun getContext(): Context {
        return support.getContext()
    }

    protected fun getDevice(): Wearable {
        return support.getWearable()
    }

    protected fun getListeners(): List<IntentListenerHandler> {
        return listeners.toList()
    }

    protected fun getQueue() = support.getQueue()

    protected fun notify(intent: Intent) {
        for (listener in listeners) listener.notify(intent)
    }

    fun removeListener(listener: IntentListenerHandler): Boolean {
        return listeners.remove(listener)
    }

    @Throws(IOException::class)
    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    fun performInitialized(taskName: String): TransactionBuilder {
        val builder = support.performInitialized(taskName)
        builder.setCallback(this)
        return builder
    }
}