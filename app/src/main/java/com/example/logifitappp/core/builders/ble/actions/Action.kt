package com.example.logifitappp.core.builders.ble.actions

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import com.example.logifitappp.core.utils.DateTimeUtils
import java.util.Date

abstract class Action(private val characteristic: BluetoothGattCharacteristic?) {
    private val createdAt = System.currentTimeMillis()

    fun getCharacteristic(): BluetoothGattCharacteristic? = characteristic

    protected fun getCreationTime(): String = DateTimeUtils.formatDateTime(Date(createdAt))

    override fun toString() = "${getCreationTime()}: ${this::class.simpleName} on characteristic: ${characteristic?.uuid.toString()}"

    abstract fun expectsResult(): Boolean
    abstract fun run(gatt: BluetoothGatt?): Boolean
}