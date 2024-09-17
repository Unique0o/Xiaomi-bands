package com.example.logifitappp.core.wearebles

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.example.logifitappp.core.handlers.WearableEventHandler

interface WearableSupport: WearableEventHandler {
    fun connect(): Boolean
    fun connectFirstTime(): Boolean
    fun dispose()
    fun getAutoReconnect(): Boolean
    fun getBluetoothAdapter(): BluetoothAdapter?
    fun getContext(): Context
    fun getScanReconnect(): Boolean
    fun getWearable(): Wearable
    fun isConnected(): Boolean
    fun setAutoReconnect(enabled: Boolean)
    fun setContext(wearable: Wearable, adapter: BluetoothAdapter?, context: Context)
    fun setScanReconnect(enabled: Boolean)
    fun useAutoConnect(): Boolean
}