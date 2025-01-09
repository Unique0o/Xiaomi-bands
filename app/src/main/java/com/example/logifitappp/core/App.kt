package com.example.logifitappp.core

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.example.logifitappp.core.broadcasters.BluetoothStateChangeReceiver
import com.example.logifitappp.core.utils.LimitedQueue
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableManager
import com.example.logifitappp.core.wearebles.WearablePreferences
import com.example.logifitappp.core.wearebles.WearableService
import com.example.logifitappp.data.AppDatabase
import com.example.logifitappp.enums.AppStatusCodeEnum
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
        const val ACTION_NEW_DATA = "com.info.logifit.pe.action.new_data"
        const val ACTION_QUIT = "com.info.logifit.pe.action.quit"
        const val EXTRA_SHOULD_REQUEST_ADDITIONAL_INFORMATION = "should_request_additional_information"
        const val FAILED_CONNECTION_WITH_WEARABLE = "com.info.logifit.pe.failed.connection.with.wearable"
        const val NOTIFICATION = "com.info.logifit.pe.notification"
        const val RELOAD_AUTHENTICATED_USER = "com.info.logifit.pe.reload.authenticated.user"
        const val REQUEST_ADDITIONAL_INFORMATION = "com.info.logifit.pe.request.additional.information"

        lateinit var context: App
            private set

        lateinit var database: AppDatabase
            private set

        var mIDSenderLookup = LimitedQueue<Int, String>(16)
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

        fun isRunningPieOrLater() = VERSION.SDK_INT >= Build.VERSION_CODES.P

        fun refreshWearables() = wearableManager.refreshPairedWearables()

        fun signalNewNotification() {
            preferences.getPreferences()
                .edit()
                .putBoolean(AppPreferences.NEW_NOTIFICATION, true)
                .apply()

            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(NOTIFICATION))
        }

        fun signalFailedConnectionWithWearable(status: AppStatusCodeEnum) {
            val intent = Intent(FAILED_CONNECTION_WITH_WEARABLE)
            intent.putExtra(Wearable.EXTRA_FAILED_CONNECTION_STATUS, status.code())

            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }

        fun signalFetchingActivityDataFinish(wearable: Wearable) {
            val intent = Intent(ACTION_NEW_DATA)
            intent.putExtra(Wearable.EXTRA_DEVICE, wearable)

            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }

        fun signalReloadAuthenticatedUser() {
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(RELOAD_AUTHENTICATED_USER))
        }

        fun signalRequestAdditionalInformation(should: Boolean) {
            val intent = Intent(REQUEST_ADDITIONAL_INFORMATION)
            intent.putExtra(EXTRA_SHOULD_REQUEST_ADDITIONAL_INFORMATION, should)

            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }

        fun supportsBluetoothLE() = context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }
}