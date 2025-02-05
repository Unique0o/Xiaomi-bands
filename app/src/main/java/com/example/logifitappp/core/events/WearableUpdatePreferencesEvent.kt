package com.example.logifitappp.core.events

import android.content.SharedPreferences
import androidx.core.content.edit

class WearableUpdatePreferencesEvent: AbstractWearableEvent {
    private val preferences = mutableMapOf<String, Any?>()

    constructor()

    constructor(preferences: Map<String, Any?>) {
        this.preferences.putAll(preferences)
    }

    constructor(key: String, value: Any?) {
        preferences[key] = value
    }

    fun update(prefs: SharedPreferences) {
        prefs.edit {
            for ((key, value) in preferences) {
                println("Updating $key = $value")

                when (value) {
                    null -> remove(key)
                    is Short -> putInt(key, value.toInt())
                    is Int -> putInt(key, value)
                    is Boolean -> putBoolean(key, value)
                    is String -> putString(key, value)
                    is Float -> putFloat(key, value)
                    is Long -> putLong(key, value)
                    is Set<*> -> putStringSet(key, value as Set<String>)
                    else -> println("Unknown preference value type ${value::class.java} for $key")
                }
            }

            apply()
        }
    }
}