package com.example.logifitappp.core

import android.content.SharedPreferences

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