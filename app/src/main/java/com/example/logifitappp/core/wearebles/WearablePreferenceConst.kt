package com.example.logifitappp.core.wearebles

import java.util.Locale

object WearablePreferenceConst {
    const val PREF_HEART_RATE_ACTIVITY_MONITORING = "heart_rate_activity_monitoring"
    const val PREF_HEART_RATE_ALERT_HIGH_THRESHOLD = "heart_rate_alert_high_threshold"
    const val PREF_HEART_RATE_ALERT_LOW_THRESHOLD = "heart_rate_alert_low_threshold"
    const val PREF_HEART_RATE_MEASUREMENT_INTERVAL = "heart_rate_measurement_interval"
    const val PREF_HEART_RATE_SLEEP_BREATHING_QUALITY_MONITORING = "heart_rate_sleep_breathing_quality_monitoring"
    const val PREF_HEART_RATE_STRESS_MONITORING = "heart_rate_stress_monitoring"
    const val PREF_HEART_RATE_STRESS_RELAXATION_REMINDER = "heart_rate_stress_relaxation_reminder"
    const val PREF_HEART_RATE_USE_FOR_SLEEP_DETECTION = "heart_rate_sleep_detection"

    const val PREF_SPO2_ALL_DAY_MONITORING = "spo2_all_day_monitoring_enabled"
    const val PREF_SPO2_LOW_ALERT_THRESHOLD = "spo2_low_alert_threshold"

    fun getPrefKnownConfig(key: String) = String.format(Locale.ROOT, "%s_is_known", key)
}