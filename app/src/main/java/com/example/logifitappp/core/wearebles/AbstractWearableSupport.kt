package com.example.logifitappp.core.wearebles

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.logifitappp.core.App
import com.example.logifitappp.core.broadcasters.CallControlBroadcastReceiver
import com.example.logifitappp.core.specs.CallSpec
import com.example.logifitappp.core.events.AbstractWearableEvent
import com.example.logifitappp.core.events.WearableBatteryInfoEvent
import com.example.logifitappp.core.events.WearableCallControlEvent
import com.example.logifitappp.core.events.WearableNotificationControlEvent
import com.example.logifitappp.core.events.WearableUpdatePreferencesEvent
import com.example.logifitappp.core.events.WearableVersionInfoEvent
import com.example.logifitappp.core.services.AppNotificationListenerService
import com.example.logifitappp.core.specs.MusicSpec
import com.example.logifitappp.core.specs.MusicStateSpec
import com.example.logifitappp.core.specs.NotificationSpec

abstract class AbstractWearableSupport: WearableSupport {
    private var adapter: BluetoothAdapter? = null
    private var autoReconnect = false
    private lateinit var context: Context
    private var scanReconnect = false
    private lateinit var wearable: Wearable

    override fun connectFirstTime(): Boolean {
        return connect()
    }

    fun evaluateWearableEvent(wearableEvent: AbstractWearableEvent) {
        when (wearableEvent) {
            is WearableBatteryInfoEvent -> handleBatteryInfoEvent(wearableEvent)
            is WearableVersionInfoEvent -> handleVersionInfoEvent(wearableEvent)
            is WearableNotificationControlEvent -> handleNotificationControlEvent(wearableEvent)
            is WearableCallControlEvent -> handleCallControlEvent(wearableEvent)
            is WearableUpdatePreferencesEvent -> handleUpdatePreferencesEvent(wearableEvent)
        }
    }

    override fun getAutoReconnect(): Boolean {
        return autoReconnect
    }

    override fun getBluetoothAdapter(): BluetoothAdapter? {
        return adapter
    }

    override fun getContext(): Context {
        return context
    }

    override fun getScanReconnect(): Boolean {
        return scanReconnect
    }

    override fun getWearable(): Wearable {
        return wearable
    }

    fun getWearablePrefs() = App.getWearablePreferences(wearable.getAddress()!!)

    protected fun handleBatteryInfoEvent(batteryInfoEvent: WearableBatteryInfoEvent) {
        println("Got BATTERY_INFO device event")

        wearable.apply {
            setBatteryLevel(batteryInfoEvent.level, batteryInfoEvent.index)
            sendDeviceUpdateIntent(getContext())
        }
    }

    private fun handleCallControlEvent(callControlEvent: WearableCallControlEvent) {
        println("Got event for CALL_CONTROL")
        val context = getContext()

        if (callControlEvent.event == WearableCallControlEvent.Event.IGNORE) {
            println("Sending intent for mute")
            Intent(context.packageName + ".MUTE_CALL").apply {
                setPackage(context.packageName)
                context.sendBroadcast(this)
            }

            return
        }

        Intent(CallControlBroadcastReceiver.ACTION_CALL_CONTROL).apply {
            putExtra("event", callControlEvent.event.ordinal)
            setPackage(context.packageName)
            context.sendBroadcast(this)
        }
    }

    private fun handleNotificationControlEvent(notificationControlEvent: WearableNotificationControlEvent) {
        val context = getContext()

        val action = when (notificationControlEvent.event) {
            WearableNotificationControlEvent.Event.DISMISS -> AppNotificationListenerService.ACTION_DISMISS
            WearableNotificationControlEvent.Event.DISMISS_ALL -> AppNotificationListenerService.ACTION_DISMISS_ALL
            WearableNotificationControlEvent.Event.OPEN -> AppNotificationListenerService.ACTION_OPEN
            WearableNotificationControlEvent.Event.MUTE -> AppNotificationListenerService.ACTION_MUTE
            WearableNotificationControlEvent.Event.REPLY -> run whenElse@ {
                notificationControlEvent.phoneNumber = notificationControlEvent.phoneNumber ?: App.mIDSenderLookup.lookup((notificationControlEvent.handle shr 4).toInt())

                if (notificationControlEvent.phoneNumber != null) {
                    println("Got notification reply for SMS from ${notificationControlEvent.phoneNumber} : ${notificationControlEvent.reply}")
                    context.getSystemService(SmsManager::class.java).sendTextMessage(notificationControlEvent.phoneNumber, null, notificationControlEvent.reply, null, null)
                } else {
                    println("Got notification reply for notification id ${notificationControlEvent.handle} : ${notificationControlEvent.reply}")

                    return@whenElse AppNotificationListenerService.ACTION_REPLY
                }

                return@whenElse null
            }
            else -> null
        }

        action?.let {
            val notificationListenerIntent = Intent(it)
            notificationListenerIntent.putExtra("handle", notificationControlEvent.handle)
            notificationListenerIntent.putExtra("title", notificationControlEvent.title)

            if (notificationControlEvent.reply != null) {
                val prefs = App.getWearableSpecificSharedPrefs(wearable.getAddress())
                val suffix = prefs?.getString("canned_reply_suffix", null)

                if (!suffix.isNullOrEmpty()) notificationControlEvent.reply += suffix

                notificationListenerIntent.putExtra("reply", notificationControlEvent.reply)
            }

            LocalBroadcastManager.getInstance(context).sendBroadcast(notificationListenerIntent)
        }
    }

    private fun handleUpdatePreferencesEvent(updatePreferencesEvent: WearableUpdatePreferencesEvent) {
        updatePreferencesEvent.update(App.getWearableSpecificSharedPrefs(wearable.getAddress())!!)
        wearable.sendDeviceUpdateIntent(getContext())
    }

    protected fun handleVersionInfoEvent(infoEvent: WearableVersionInfoEvent) {
        wearable.apply {
            setFirmwareVersion(infoEvent.firmwareVersion)
            setModel(infoEvent.model)
            sendDeviceUpdateIntent(getContext())
        }
    }

    override fun isConnected(): Boolean {
        return wearable.isConnected()
    }

    fun isInitialized(): Boolean {
        return wearable.isInitialized()
    }

    override fun onDeleteNotification(id: Int) {

    }

    override fun onFetchRecordedData(dataTypes: Int) {

    }

    override fun onNotification(notificationSpec: NotificationSpec) {

    }

    override fun onSetCallState(callSpec: CallSpec) {

    }

    override fun onSetMusicInfo(musicSpec: MusicSpec?) {

    }

    override fun onSetMusicState(stateSpec: MusicStateSpec) {

    }

    override fun setAutoReconnect(enabled: Boolean) {
        autoReconnect = enabled
    }

    override fun setContext(wearable: Wearable, adapter: BluetoothAdapter?, context: Context) {
        this.adapter = adapter
        this.context = context
        this.wearable = wearable
    }

    override fun setScanReconnect(enabled: Boolean) {
        scanReconnect = enabled
    }
}