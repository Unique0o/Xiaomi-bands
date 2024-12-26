package com.example.logifitappp.core.builders.ble.actions

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import androidx.annotation.RequiresPermission
import com.example.logifitappp.core.wearebles.huami.HuamiService

abstract class StopNotificationAction(private val alertLevelCharacteristic: BluetoothGattCharacteristic?): AbortTransactionAction() {
    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    override fun run(gatt: BluetoothGatt?): Boolean {
        if (!super.run(gatt)) {
            alertLevelCharacteristic?.let {
                it.value = byteArrayOf(HuamiService.ALERT_LEVEL_NONE.toByte())
                gatt?.writeCharacteristic(it)
            }

            return false
        }

        return true
    }
}