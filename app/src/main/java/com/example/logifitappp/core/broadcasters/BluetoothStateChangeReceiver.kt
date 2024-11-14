package com.example.logifitappp.core.broadcasters

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.logifitappp.core.App
import com.example.logifitappp.core.services.WearableCommunicationService

class BluetoothStateChangeReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)) {
                    BluetoothAdapter.STATE_OFF -> {
                        if (!WearableCommunicationService.isRunning(context)) {
                            println("WearableCommunicationService not running, ignoring bluetooth off")
                            return
                        }

                        println("Bluetooth turned off => disconnecting...")
                        App.wearableService.disconnect()
                    }
                }
            }
        }
    }
}