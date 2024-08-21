package com.example.logifitappp.core.bluetooth

import android.bluetooth.BluetoothDevice
import android.os.ParcelUuid

class ScanEvent(private val wearable: BluetoothDevice, private val rssi: Short, private val services: Array<ParcelUuid>?) {
    fun getRssi(): Short {
        return rssi
    }

    fun getServices(): Array<ParcelUuid>? {
        return services
    }

    fun getWearable(): BluetoothDevice {
        return wearable
    }
}