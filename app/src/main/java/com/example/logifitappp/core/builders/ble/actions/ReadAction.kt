package com.example.logifitappp.core.builders.ble.actions

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import androidx.annotation.RequiresPermission

class ReadAction(characteristic: BluetoothGattCharacteristic): Action(characteristic) {
    override fun expectsResult() = true

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    override fun run(gatt: BluetoothGatt?): Boolean {
        val properties = getCharacteristic()!!.properties

        return if ((properties and BluetoothGattCharacteristic.PROPERTY_READ) > 0) gatt?.readCharacteristic(getCharacteristic()) ?: false
        else false
    }

}