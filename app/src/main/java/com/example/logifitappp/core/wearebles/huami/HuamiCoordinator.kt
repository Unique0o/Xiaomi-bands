package com.example.logifitappp.core.wearebles.huami

import com.example.logifitappp.core.App
import com.example.logifitappp.core.Preferences
import com.example.logifitappp.core.wearebles.AbstractBleWearableCoordinator

abstract class HuamiCoordinator: AbstractBleWearableCoordinator() {
    override fun suggestUnbindBeforePair() = false

    override fun supportsActivityDataFetching() = true

    companion object {
        fun getKeepActivityDataOnDevice(mac: String): Boolean {
            val prefs = Preferences(App.getWearableSpecificSharedPrefs(mac)!!)
            return prefs.getBoolean("keep_activity_data_on_device", false)
        }
    }
}