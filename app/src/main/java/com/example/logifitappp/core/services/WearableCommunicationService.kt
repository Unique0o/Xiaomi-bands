package com.example.logifitappp.core.services

import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import com.example.logifitappp.core.App
import com.example.logifitappp.core.AppPreferences
import com.example.logifitappp.core.bluetooth.BluetoothConnector
import com.example.logifitappp.exceptions.WearableNotFoundException
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableService

class WearableCommunicationService: Service(), SharedPreferences.OnSharedPreferenceChangeListener {
    private fun handleAction(intent: Intent, action: String?, wearable: Wearable) {
        if (WearableService.ACTION_DISCONNECT == action) {
            BluetoothConnector.disconnect(wearable, this)
            return
        }

        val support = BluetoothConnector.getWearableSupport(wearable)

        when (action) {
            WearableService.ACTION_FETCH_RECORDED_DATA -> {
                val dataTypes = intent.getIntExtra(WearableService.EXTRA_RECORDED_DATA_TYPES, 0)
                support.onFetchRecordedData(dataTypes)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            AppPreferences.DEVICE_AUTO_RECONNECT -> {
                BluetoothConnector.getStructs().forEach {
                    val autoReconnect = App.preferences.getAutoReconnect(it.wearable!!)
                    it.support?.setAutoReconnect(autoReconnect)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) {
            println("no intent")
            return START_STICKY
        }

        if (intent.action == null) {
            println("no action")
            return START_STICKY
        }

        println("Service start command: ${intent.action}")

        val wearable = intent.getParcelableExtra<Wearable>(Wearable.EXTRA_DEVICE)

        when (intent.action) {
            WearableService.ACTION_CONNECT -> {
                val firstTime = intent.getBooleanExtra(WearableService.EXTRA_CONNECT_FIRST_TIME, false)
                BluetoothConnector.connect(wearable, firstTime)
            }

            else -> {
                BluetoothConnector.getTargetedWearables(intent.action, wearable).forEach {
                    try {
                        handleAction(intent, intent.action, it)
                    } catch (e: WearableNotFoundException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        println("An exception was raised while handling the action ${intent.action} for the device $it:")
                    }
                }
            }
        }

        return START_STICKY
    }
}