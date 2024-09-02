package com.example.logifitappp.core.wearebles

import android.content.SharedPreferences
import com.example.logifitappp.core.Preferences

class WearablePreferences(prefs: SharedPreferences) : Preferences(prefs) {
    fun getBatteryPollingEnabled() = getBoolean(WearableSettingPreferenceConstants.PREF_BATTERY_POLLING_ENABLE, true)

    fun getBatteryPollingIntervalMinutes() = getInt(WearableSettingPreferenceConstants.PREF_BATTERY_POLLING_INTERVAL, 15)
}