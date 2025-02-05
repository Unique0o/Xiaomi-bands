package com.example.logifitappp.core.builders.ble.profiles

import android.bluetooth.BluetoothGattCharacteristic
import kotlin.math.max
import kotlin.math.min

object CharacteristicValueDecoder {
    fun decodePercent(characteristic: BluetoothGattCharacteristic): Int {
        var percent = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0)

        if (percent !in 0 .. 100) {
            println("Unexpected percent value: $percent : $characteristic")
            percent = min(100, max(0, percent))
        }

        return percent
    }
}