package com.example.logifitappp.core.builders.ble.actions

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattServer
import java.text.DateFormat
import java.util.Date

abstract class ServerAction(private val device: BluetoothDevice) {
    private val createdAt = System.currentTimeMillis()

    protected fun getCreationTime(): String = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(Date(createdAt))

    fun getDevice(): BluetoothDevice {
        return device
    }

    override fun toString() = "${getCreationTime()}: ${this::class.simpleName} on device: ${getDevice().address}"

    abstract fun expectsResult(): Boolean
    abstract fun run(server: BluetoothGattServer?): Boolean
}