package com.example.logifitappp.core.builders.ble.actions

import android.bluetooth.BluetoothGatt
import android.content.Context
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.enums.WearableUpdateSubjectEnum

class SetWearableStateAction(private val wearable: Wearable, private val state: Wearable.State, private val context: Context): PlainAction() {
    override fun run(gatt: BluetoothGatt?): Boolean {
        wearable.setState(state)
        wearable.sendDeviceUpdateIntent(context, WearableUpdateSubjectEnum.DEVICE_STATE)
        return true
    }

    override fun toString(): String = "${super.toString()} to $state"
}