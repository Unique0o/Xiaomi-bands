package com.example.logifitappp.core.wearebles

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.example.logifitappp.core.App
import com.example.logifitappp.core.CallSpec
import com.example.logifitappp.core.events.AbstractWearableEvent
import com.example.logifitappp.core.events.WearableBatteryInfoEvent
import com.example.logifitappp.core.events.WearableUpdatePreferencesEvent
import com.example.logifitappp.core.events.WearableVersionInfoEvent

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

    private fun handleBatteryInfoEvent(batteryInfoEvent: WearableBatteryInfoEvent) {
        println("Got BATTERY_INFO device event")

        wearable.apply {
            setBatteryLevel(batteryInfoEvent.level, batteryInfoEvent.index)
            sendDeviceUpdateIntent(getContext())
        }
    }

    private fun handleUpdatePreferencesEvent(updatePreferencesEvent: WearableUpdatePreferencesEvent) {
        updatePreferencesEvent.update(App.getWearableSpecificSharedPrefs(wearable.getAddress())!!)
        wearable.sendDeviceUpdateIntent(getContext())
    }

    private fun handleVersionInfoEvent(infoEvent: WearableVersionInfoEvent) {
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

    override fun onFetchRecordedData(dataTypes: Int) {

    }

    override fun onSetCallState(callSpec: CallSpec) {

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