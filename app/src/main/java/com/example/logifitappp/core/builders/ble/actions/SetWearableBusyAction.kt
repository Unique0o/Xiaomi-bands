package com.example.logifitappp.core.builders.ble.actions

import android.bluetooth.BluetoothGatt
import android.content.Context
import com.example.logifitappp.core.wearebles.Wearable

class SetWearableBusyAction(
    private val wearable: Wearable,
    private val busyTask: String,
    private val context: Context
): PlainAction() {
    override fun run(gatt: BluetoothGatt?): Boolean {
        wearable.setBusyTask(busyTask)
        wearable.sendDeviceUpdateIntent(context)

        return true
    }

    override fun toString() = "${getCreationTime()}: ${javaClass.name}: $busyTask"
}