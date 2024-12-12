package com.example.logifitappp.core

import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

open class Preferences(prefs: SharedPreferences) {
    init {
        preferences = prefs
    }

    fun getBoolean(key: String, default: Boolean): Boolean {
        try {
            return preferences.getBoolean(key, default)
        } catch (e: Exception) {
            try {
                val value = preferences.getString(key, default.toString())

                if (value.isNullOrEmpty()) return default

                return value.toBoolean()
            } catch (ex: Exception) {
                return default
            }
        }
    }

    fun getInt(key: String, default: Int): Int {
        try {
            return preferences.getInt(key, default)
        } catch (e: Exception) {
            try {
                val value = preferences.getString(key, default.toString())

                if (value.isNullOrEmpty()) return default

                return value.toInt()
            } catch (ex: Exception) {
                return default
            }
        }
    }

    fun getLocalTime(key: String, defaultValue: String?): LocalTime {
        val time = getString(key, defaultValue!!)

        val df = SimpleDateFormat("HH:mm", Locale.ROOT)

        try {
            val parse = df.parse(time)
            val calendar = GregorianCalendar.getInstance()

            if (parse != null) calendar.time = parse

            return LocalTime.of(
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                0
            )
        } catch (e: java.lang.Exception) {
            println("Error reading localtime preference value: $key; returning default current time $e")
        }

        return LocalTime.now()
    }

    fun getPreferences(): SharedPreferences {
        return preferences
    }

    fun getString(key: String, default: String): String {
        val value = preferences.getString(key, default)

        if (value.isNullOrEmpty()) return default

        return value
    }

    fun getStringSet(key: String, default: Set<String>): Set<String> {
        val value = preferences.getStringSet(key, default)

        if (value.isNullOrEmpty()) return default

        return value
    }

    companion object {
        private lateinit var preferences: SharedPreferences
    }
}