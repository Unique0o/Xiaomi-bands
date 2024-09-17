package com.example.logifitappp.core.builders.ble.actions

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import androidx.annotation.RequiresPermission

class WriteAction(characteristic: BluetoothGattCharacteristic?, private val value: ByteArray) : Action(characteristic) {
    override fun expectsResult() = true

    fun getValue() = value

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    override fun run(gatt: BluetoothGatt?): Boolean {
        val characteristic = getCharacteristic()
        val properties = characteristic!!.properties

        if ((properties and BluetoothGattCharacteristic.PROPERTY_WRITE) > 0 || (properties and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0) {
            return writeValue(gatt!!, characteristic, value)
        }

        return false
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    protected fun writeValue(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray): Boolean {
        println("writing to characteristic: ${characteristic.uuid}: [${value.contentToString()}]")

        if (characteristic.setValue(value)) return gatt.writeCharacteristic(characteristic)

        return false
    }
}