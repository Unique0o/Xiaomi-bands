package com.example.logifitappp.core

import android.app.Application
import android.content.pm.PackageManager
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableService

class App: Application() {
    init {
        context = this
    }

    override fun onCreate() {
        super.onCreate()

        wearableService = WearableService(this)
    }

    companion object {
        lateinit var context: App
        lateinit var wearableService: WearableService

        fun getWearableServiceTo(wearable: Wearable): WearableService {
            return wearableService.forDevice(wearable)
        }

        fun supportsBluetoothLE(): Boolean {
            return context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
        }
    }
}