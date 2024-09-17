package com.example.logifitappp.core.services

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.example.logifitappp.core.CallSpec
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableSupport
import com.example.logifitappp.core.wearebles.WearableSupportFlagEnum
import java.util.EnumSet

class WearableSupportService(private val delegate: WearableSupport, private val flags: EnumSet<WearableSupportFlagEnum>): WearableSupport {
    private fun checkBusy(notificationKind: String): Boolean {
        if (!flags.contains(WearableSupportFlagEnum.BUSY_CHECKING)) return false

        if (getWearable().isBusy()) {
            println("Ignoring $notificationKind because we're busy with ${getWearable().getBusyTask()}")
            return true
        }

        return false
    }

    override fun connect(): Boolean {
        return delegate.connect()
    }

    override fun connectFirstTime(): Boolean {
        return delegate.connectFirstTime()
    }

    override fun dispose() {
        delegate.dispose()
    }

    override fun getAutoReconnect(): Boolean {
        return delegate.getAutoReconnect()
    }

    override fun getBluetoothAdapter(): BluetoothAdapter? {
        return delegate.getBluetoothAdapter()
    }

    override fun getContext(): Context {
        return delegate.getContext()
    }

    override fun getScanReconnect(): Boolean {
        return delegate.getScanReconnect()
    }

    override fun getWearable(): Wearable {
        return delegate.getWearable()
    }

    override fun isConnected(): Boolean {
        return delegate.isConnected()
    }

    override fun setAutoReconnect(enabled: Boolean) {
        delegate.setAutoReconnect(enabled)
    }

    override fun setContext(wearable: Wearable, adapter: BluetoothAdapter?, context: Context) {
        delegate.setContext(wearable, adapter, context)
    }

    override fun setScanReconnect(enabled: Boolean) {
        delegate.setScanReconnect(enabled)
    }

    override fun useAutoConnect(): Boolean {
        return delegate.useAutoConnect()
    }

    override fun onFetchRecordedData(dataTypes: Int) {
        if (checkBusy("fetch activity data")) return

        delegate.onFetchRecordedData(dataTypes)
    }

    override fun onSetCallState(callSpec: CallSpec) {
        if (checkBusy("set call state")) return

        delegate.onSetCallState(callSpec)
    }
}