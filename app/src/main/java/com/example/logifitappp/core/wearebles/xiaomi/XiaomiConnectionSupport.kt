package com.example.logifitappp.core.wearebles.xiaomi

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.example.logifitappp.core.wearebles.Wearable
import nodomain.freeyourgadget.gadgetbridge.proto.xiaomi.XiaomiProto

interface XiaomiConnectionSupport {
    fun connect(): Boolean
    fun dispose()
    fun onAuthSuccess()
    fun sendCommand(taskName: String, command: XiaomiProto.Command)
    fun setAutoReconnect(enabled: Boolean)
    fun setContext(wearable: Wearable, adapter: BluetoothAdapter, context: Context)
}