package com.example.logifitappp.core

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableManager
import com.example.logifitappp.core.wearebles.WearablePreferences
import com.example.logifitappp.core.wearebles.WearableService

class App: Application() {
    init {
        context = this
    }

    override fun onCreate() {
        super.onCreate()

        preferences = AppPreferences(PreferenceManager.getDefaultSharedPreferences(context))
        wearableManager = WearableManager(this)
        wearableService = WearableService(this)
    }

    companion object {
        const val ACTION_NEW_DATA = "com.info.logifit.pe.action.quit"

        lateinit var context: App
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

        fun isRunningMarshmallowOrLater() = VERSION.SDK_INT >= Build.VERSION_CODES.O

        fun signalActivityDataFinish(wearable: Wearable) {
            val intent = Intent(ACTION_NEW_DATA)
            intent.putExtra(Wearable.EXTRA_DEVICE, wearable)

            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }

        fun supportsBluetoothLE() = context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }
}