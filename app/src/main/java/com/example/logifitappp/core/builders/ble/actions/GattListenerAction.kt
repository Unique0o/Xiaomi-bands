package com.example.logifitappp.core.builders.ble.actions

import com.example.logifitappp.core.handlers.BluetoothGattCallbackHandler

interface GattListenerAction {
    fun getGattCallback(): BluetoothGattCallbackHandler
}