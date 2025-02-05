package com.example.logifitappp.core.wearebles.xiaomi

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.example.logifitappp.core.wearebles.Wearable
import nodomain.freeyourgadget.gadgetbridge.proto.xiaomi.XiaomiProto

abstract class XiaomiConnectionSupport {
    open fun onAuthSuccess() {}

    open fun setAutoReconnect(enabled: Boolean) {}

    abstract fun connect(): Boolean
    abstract fun dispose()
    abstract fun sendCommand(taskName: String, command: XiaomiProto.Command)
    abstract fun setContext(wearable: Wearable, adapter: BluetoothAdapter, context: Context)
}