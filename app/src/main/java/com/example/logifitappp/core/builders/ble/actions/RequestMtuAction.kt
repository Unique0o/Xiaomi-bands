package com.example.logifitappp.core.builders.ble.actions

import android.bluetooth.BluetoothGatt
import androidx.annotation.RequiresPermission

class RequestMtuAction(private val mtu: Int): Action(null) {
    override fun expectsResult() = true

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    override fun run(gatt: BluetoothGatt?) = gatt!!.requestMtu(mtu)
}