package com.example.logifitappp.core.wearebles

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.logifitappp.core.App
import com.example.logifitappp.data.models.WearableModel
import java.util.Collections

class WearableManager(private val context: Context) {
    private val receiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.action) {
                Wearable.ACTION_DEVICE_CHANGED -> {
                    val wearable = intent.getParcelableExtra<Wearable>(Wearable.EXTRA_DEVICE)!!

                    if (wearable.getAddress() != null) {
                        val index = wearables.indexOf(wearable)

                        if (index >= 0) wearables[index].copyFromDevice(wearable)
                        else wearables.add(wearable)

                        if (wearable.isInitialized()) storeWearable(wearable)
                    }

                    refreshPairedWearables()
                }
            }
        }
    }

    private val wearables = mutableListOf<Wearable>()

    init {
        val filter = IntentFilter()
        filter.addAction(Wearable.ACTION_DEVICE_CHANGED)
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter)

        refreshPairedWearables()
    }

    fun getWearables(): List<Wearable> = Collections.unmodifiableList(wearables)

    private fun notifyWearablesChanged() {
        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(ACTION_DEVICES_CHANGED))
    }

    private fun refreshPairedWearables() {
        val availableWearables = WearableHelper.getInstance().getAvailableWearables()
        wearables.retainAll(availableWearables)

        availableWearables.forEach {
            if (!wearables.contains(it)) wearables.add(it)
        }

        println("paired wearables: $wearables")

        notifyWearablesChanged()
    }

    private fun storeWearable(wearable: Wearable) {
        val model = WearableModel()
        model.firmwareVersion = wearable.getFirmwareVersion()
        model.mac = wearable.getAddress()!!
        model.name = wearable.getName()!!
        model.typeName = wearable.getType().name

        App.database.wearableDao().store(model)
    }

    companion object {
        const val ACTION_DEVICES_CHANGED = "com.info.logifit.pe.action.devices_changed"
    }
}