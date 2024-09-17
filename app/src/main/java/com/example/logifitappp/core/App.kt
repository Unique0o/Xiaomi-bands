package com.example.logifitappp.core

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableManager
import com.example.logifitappp.core.wearebles.WearablePreferences
import com.example.logifitappp.core.wearebles.WearableService
import com.example.logifitappp.data.AppDatabase

class App: Application() {
    init {
        context = this
    }

    override fun onCreate() {
        super.onCreate()

        database = Room
            .databaseBuilder(this, AppDatabase::class.java, "logifit_db")
            .allowMainThreadQueries()
            .build()

        preferences = AppPreferences(PreferenceManager.getDefaultSharedPreferences(context))
        wearableManager = WearableManager(this)
        wearableService = WearableService(this)
    }

    companion object {
        const val ACTION_NEW_DATA = "com.info.logifit.pe.action.quit"

        lateinit var context: App
        lateinit var database: AppDatabase
        lateinit var preferences: AppPreferences

        @SuppressLint("StaticFieldLeak")
        lateinit var wearableManager: WearableManager

        @SuppressLint("StaticFieldLeak")
        lateinit var wearableService: WearableService

        fun getWearableServiceTo(wearable: Wearable) = wearableService.forDevice(wearable)

        fun getWearablePreferences(wearableIdentifier: String) = WearablePreferences(getWearableSpecificSharedPrefs(wearableIdentifier)!!)

        fun getWearableSpecificSharedPrefs(wearableIdentifier: String?): SharedPreferences? {
            if (wearableIdentifier.isNullOrEmpty()) return null

            return context.getSharedPreferences("wearable_settings_$wearableIdentifier", Context.MODE_PRIVATE)
        }

        fun signalActivityDataFinish(wearable: Wearable) {
            val intent = Intent(ACTION_NEW_DATA)
            intent.putExtra(Wearable.EXTRA_DEVICE, wearable)

            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }

        fun supportsBluetoothLE() = context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }
}