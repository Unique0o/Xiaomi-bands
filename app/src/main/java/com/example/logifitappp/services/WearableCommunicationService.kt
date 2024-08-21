package com.example.logifitappp.services

import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder

class WearableCommunicationService: Service(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }
}