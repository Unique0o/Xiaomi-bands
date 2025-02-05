package com.example.logifitappp.core.utils

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothStatusCodes
import android.os.Build
import android.os.Build.VERSION
import androidx.annotation.RequiresPermission

@RequiresPermission("android.permission.BLUETOOTH_CONNECT")
fun BluetoothGatt.fnWriteCharacteristic(characteristic: BluetoothGattCharacteristic, payload: ByteArray): Boolean {
    when {
        VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> return writeCharacteristic(characteristic, payload, characteristic.writeType) == BluetoothStatusCodes.SUCCESS
        else -> {
            @Suppress("DEPRECATION")
            if (characteristic.setValue(payload)) return writeCharacteristic(characteristic)

            return false
        }
    }
}

@RequiresPermission("android.permission.BLUETOOTH_CONNECT")
fun BluetoothGatt.fnWriteDescriptor(descriptor: BluetoothGattDescriptor, payload: ByteArray): Boolean {
    when {
        VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> return writeDescriptor(descriptor, payload) == BluetoothStatusCodes.SUCCESS
        else -> {
            @Suppress("DEPRECATION")
            if (descriptor.setValue(payload)) return writeDescriptor(descriptor)

            return false
        }
    }
}