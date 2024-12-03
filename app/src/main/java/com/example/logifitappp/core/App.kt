package com.example.logifitappp.core

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.example.logifitappp.core.broadcasters.BluetoothStateChangeReceiver
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableManager
import com.example.logifitappp.core.wearebles.WearablePreferences
import com.example.logifitappp.core.wearebles.WearableService
import com.example.logifitappp.data.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
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

        registerReceiver(BluetoothStateChangeReceiver(), IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    companion object {
        const val ACTION_NEW_DATA = "com.info.logifit.pe.action.quit"
        const val AUTHENTICATION_KEY_FAILED = "com.info.logifit.pe.authentication.key.failed"
        const val NOTIFICATION = "com.info.logifit.pe.notification"
        const val RELOAD_AUTHENTICATED_USER = "com.info.logifit.pe.reload.authenticated.user"

        lateinit var context: App
            private set

        lateinit var database: AppDatabase
            private set

        lateinit var preferences: AppPreferences
            private set

        @SuppressLint("StaticFieldLeak")
        lateinit var wearableManager: WearableManager
            private set

        @SuppressLint("StaticFieldLeak")
        lateinit var wearableService: WearableService
            private set

        fun getWearableServiceTo(wearable: Wearable) = wearableService.forDevice(wearable)

        fun getWearablePreferences(wearableIdentifier: String) = WearablePreferences(getWearableSpecificSharedPrefs(wearableIdentifier)!!)

        fun getWearableSpecificSharedPrefs(wearableIdentifier: String?): SharedPreferences? {
            if (wearableIdentifier.isNullOrEmpty()) return null

            return context.getSharedPreferences("wearable_settings_$wearableIdentifier", Context.MODE_PRIVATE)
        }

        fun signalNewNotification() {
            preferences.getPreferences()
                .edit()
                .putBoolean(AppPreferences.NEW_NOTIFICATION, true)
                .apply()

            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(NOTIFICATION))
        }

        fun signalAuthenticationKeyFailed() {
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(AUTHENTICATION_KEY_FAILED))
        }

        fun signalFetchingActivityDataFinish(wearable: Wearable) {
            val intent = Intent(ACTION_NEW_DATA)
            intent.putExtra(Wearable.EXTRA_DEVICE, wearable)

            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }

        fun signalReloadAuthenticatedUser() {
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(RELOAD_AUTHENTICATED_USER))
        }

        fun supportsBluetoothLE() = context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }
}