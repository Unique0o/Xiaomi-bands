package com.example.logifitappp.core.builders.ble.actions

import android.bluetooth.BluetoothGatt

class WaitAction(private val millis: Int): PlainAction() {
    override fun run(gatt: BluetoothGatt?): Boolean {
        try {
            Thread.sleep(millis.toLong())
            return true
        } catch (e: InterruptedException) {
            return false
        }
    }
}