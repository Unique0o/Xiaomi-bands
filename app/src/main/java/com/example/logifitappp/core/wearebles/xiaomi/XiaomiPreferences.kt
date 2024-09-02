package com.example.logifitappp.core.wearebles.xiaomi

import com.example.logifitappp.core.App
import com.example.logifitappp.core.Preferences
import com.example.logifitappp.core.wearebles.Wearable

object XiaomiPreferences {
    const val FEAT_DEVICE_ACTIONS = "feat_device_actions"

    fun keepActivityDataOnDevice(wearable: Wearable): Boolean {
        val preferences = Preferences(App.getWearableSpecificSharedPrefs(wearable.getAddress())!!)
        return preferences.getBoolean("keep_activity_data_on_device", false)
    }
}