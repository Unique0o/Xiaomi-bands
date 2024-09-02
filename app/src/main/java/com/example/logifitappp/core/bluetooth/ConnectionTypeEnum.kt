package com.example.logifitappp.core.bluetooth

enum class ConnectionTypeEnum(private val useBluetoothClassic: Boolean, private val usesBluetoothLE: Boolean) {
    BLE(false, true),
    BOTH(true, true),
    BT_CLASSIC(true, false);

    fun usesBluetoothClassic(): Boolean {
        return useBluetoothClassic
    }

    fun usesBluetoothLE(): Boolean {
        return usesBluetoothLE
    }
}