package com.example.logifitappp.core.wearebles

import android.app.Service
import android.content.Context
import android.content.Intent
import com.example.logifitappp.services.WearableCommunicationService

open class WearableService(private val context: Context, private val wearable: Wearable?) {
    private var serviceClass: Class<out Service> = WearableCommunicationService::class.java

    constructor(context: Context): this(context, null)

    fun connect() {
        connect(false)
    }

    fun connect(firstTime: Boolean) {
        invokeService(createIntent().setAction(ACTION_CONNECT).putExtra(EXTRA_CONNECT_FIRST_TIME, firstTime))
    }

    private fun createIntent(): Intent {
        return Intent(context, serviceClass)
    }

    fun disconnect() {
        invokeService(createIntent().setAction(ACTION_DISCONNECT))
    }

    private fun invokeService(intent: Intent) {
        if (wearable != null) intent.putExtra(Wearable.EXTRA_DEVICE, wearable)

        try {
            context.startService(intent)
        } catch (e: IllegalStateException) {
            println("IllegalStateException during startService (${intent.action})")
        }
    }

    fun forDevice(wearable: Wearable): WearableService {
        return WearableService(context, wearable)
    }

    companion object {
        const val ACTION_CONNECT = "action.connect"
        const val ACTION_DISCONNECT = "action.disconnect"

        const val EXTRA_CONNECT_FIRST_TIME = "connect_first_time"
    }
}