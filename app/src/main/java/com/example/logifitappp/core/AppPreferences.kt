package com.example.logifitappp.core

import android.content.SharedPreferences
import android.text.format.DateFormat
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableSettingPreferenceConstants

open class AppPreferences(prefs: SharedPreferences): Preferences(prefs) {
    fun getAutoReconnect(wearable: Wearable): Boolean {
        return App.getWearableSpecificSharedPrefs(wearable.getAddress())!!.getBoolean(DEVICE_AUTO_RECONNECT, true)
    }

    fun getAutoReconnectByScan(): Boolean {
        return getBoolean(RECONNECT_SCAN_KEY, false)
    }

    fun getTimeFormat(): String {
        var timeFormat = getString(WearableSettingPreferenceConstants.PREF_TIME_FORMAT, WearableSettingPreferenceConstants.PREF_TIME_FORMAT_AUTO)

        if (WearableSettingPreferenceConstants.PREF_TIME_FORMAT_AUTO == timeFormat) {
            timeFormat =
                if (DateFormat.is24HourFormat(App.context)) WearableSettingPreferenceConstants.PREF_TIME_FORMAT_24H
                else WearableSettingPreferenceConstants.PREF_TIME_FORMAT_12H
        }

        return timeFormat
    }

    companion object {
        const val DEVICE_AUTO_RECONNECT = "prefs_key_device_auto_reconnect"
        const val FIREBASE_NOTIFICATION_TOKEN = "firebase_notification_token"
        const val LAST_DEVICE_ADDRESSES = "last_device_addresses"
        const val RECONNECT_ONLY_TO_CONNECTED = "general_reconnectonlytoconnected"
        const val RECONNECT_SCAN_KEY = "prefs_general_key_auto_reconnect_scan"
    }
}