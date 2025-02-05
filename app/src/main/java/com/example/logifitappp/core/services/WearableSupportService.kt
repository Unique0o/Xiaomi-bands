package com.example.logifitappp.core.services

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.example.logifitappp.core.specs.CallSpec
import com.example.logifitappp.core.specs.MusicSpec
import com.example.logifitappp.core.specs.MusicStateSpec
import com.example.logifitappp.core.specs.NotificationSpec
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableSupport
import com.example.logifitappp.enums.WearableSupportFlagEnum
import java.util.EnumSet

class WearableSupportService(private val delegate: WearableSupport, private val flags: EnumSet<WearableSupportFlagEnum>): WearableSupport {
    private var lastNotificationKind: String? = null
    private var lastNotificationTime = 0L
    private val throttlingThreshold = 1000L

    private fun checkBusy(notificationKind: String): Boolean {
        if (!flags.contains(WearableSupportFlagEnum.BUSY_CHECKING)) return false

        if (getWearable().isBusy()) {
            println("Ignoring $notificationKind because we're busy with ${getWearable().getBusyTask()}")
            return true
        }

        return false
    }

    private fun checkThrottle(notificationKind: String?): Boolean {
        if (!flags.contains(WearableSupportFlagEnum.THROTTLING)) return false

        val currentTime = System.currentTimeMillis()

        if ((currentTime - lastNotificationTime) < throttlingThreshold) {
            if (notificationKind != null && notificationKind == lastNotificationKind) {
                return true.also { println("Ignoring $notificationKind because of throttling threshold reached") }
            }
        }

        lastNotificationTime = currentTime
        lastNotificationKind = notificationKind
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

    override fun onDeleteNotification(id: Int) {
        delegate.onDeleteNotification(id)
    }

    override fun onFetchRecordedData(dataTypes: Int) {
        if (checkBusy("fetch activity data")) return

        delegate.onFetchRecordedData(dataTypes)
    }

    override fun onNotification(notificationSpec: NotificationSpec) {
        if (checkBusy("generic notification") || checkThrottle("generic notification")) return

        delegate.onNotification(notificationSpec)
    }

    override fun onSetCallState(callSpec: CallSpec) {
        if (checkBusy("set call state")) return

        delegate.onSetCallState(callSpec)
    }

    override fun onSetMusicInfo(musicSpec: MusicSpec?) {
        if (checkBusy("set music info")) return

        delegate.onSetMusicInfo(musicSpec)
    }

    override fun onSetMusicState(stateSpec: MusicStateSpec) {
        if (checkBusy("set music state")) return

        delegate.onSetMusicState(stateSpec)
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
}