package com.example.logifitappp.core.wearebles.huami.miband

import com.example.logifitappp.core.Preferences
import com.example.logifitappp.core.wearebles.WearableVersion

object MiBandConst {
    const val PREF_MI2_ENABLE_TEXT_NOTIFICATIONS = "mi2_enable_text_notifications"

    const val ORIGIN_INCOMING_CALL = "incoming_call"

    const val DEFAULT_VALUE_VIBRATION_COUNT = 3
    const val DEFAULT_VALUE_VIBRATION_PROFILE = "short"

    const val VIBRATION_COUNT = "mi_vibration_count"
    const val VIBRATION_PROFILE = "mi_vibration_profile"

    val MI2_FW_VERSION_MIN_TEXT_NOTIFICATIONS = WearableVersion("1.0.1.28")

    fun getNotificationPrefIntValue(pref: String, origin: String, prefs: Preferences, defaultValue: Int): Int {
        return prefs.getInt(getNotificationPrefKey(pref, origin), defaultValue)
    }

    private fun getNotificationPrefKey(pref: String, origin: String) = "${pref}_$origin"

    fun getNotificationPrefStringValue(pref: String, origin: String, prefs: Preferences, defaultValue: String): String {
        return prefs.getString(getNotificationPrefKey(pref, origin), defaultValue)
    }
}