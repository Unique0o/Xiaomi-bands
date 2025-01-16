package com.example.logifitappp.core.builders.ble

import android.bluetooth.BluetoothGattCharacteristic
import com.example.logifitappp.core.bluetooth.services.BleQueue
import com.example.logifitappp.core.builders.ble.actions.Action
import com.example.logifitappp.core.builders.ble.actions.NotifyAction
import com.example.logifitappp.core.builders.ble.actions.ReadAction
import com.example.logifitappp.core.builders.ble.actions.RequestMtuAction
import com.example.logifitappp.core.builders.ble.actions.WaitAction
import com.example.logifitappp.core.builders.ble.actions.WriteAction
import com.example.logifitappp.core.handlers.BluetoothGattCallbackHandler

open class TransactionBuilder(taskName: String) {
    var transaction = Transaction(taskName)
        private set

    private var queued = false

    fun add(action: Action) {
        transaction.add(action)
    }

    private fun createNotifyAction(characteristic: BluetoothGattCharacteristic, enable: Boolean) = NotifyAction(characteristic, enable)

    fun notify(characteristic: BluetoothGattCharacteristic?, enable: Boolean) {
        if (characteristic == null) {
            println("Unable to notify characteristic: null")
            return
        }

        add(createNotifyAction(characteristic, enable))
    }

    fun setCallback(callback: BluetoothGattCallbackHandler?) {
        transaction.callbackHandler = callback
    }

    fun queue(queue: BleQueue) {
        if (queued) throw IllegalStateException("This builder had already been queued. You must not reuse it.")

        queued = true
        queue.add(transaction)
    }

    fun read(characteristic: BluetoothGattCharacteristic?) {
        if (characteristic == null) {
            println("Unable to read characteristic: null")
            return
        }

        add(ReadAction(characteristic))
    }

    fun requestMtu(mtu: Int) {
        add(RequestMtuAction(mtu))
    }

    fun wait(millis: Int) {
        add(WaitAction(millis))
    }

    fun write(characteristic: BluetoothGattCharacteristic?, payload: ByteArray) {
        if (characteristic == null) {
            println("Unable to write characteristic: null")
            return
        }

        add(WriteAction(characteristic, payload))
    }
}