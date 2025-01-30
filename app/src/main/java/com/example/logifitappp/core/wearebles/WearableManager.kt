package com.example.logifitappp.core.wearebles

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.logifitappp.core.App
import com.example.logifitappp.core.utils.parcelableExtra
import com.example.logifitappp.data.models.WearableModel
import com.example.logifitappp.data.remote.dto.requests.AssociateWearableRequest
import com.example.logifitappp.di.components.WearableComponent
import com.example.logifitappp.domain.service.WearableService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

class WearableManager(private val context: Context) {
    @Inject
    lateinit var wearableService: WearableService

    private val receiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.action) {
                Wearable.ACTION_DEVICE_CHANGED -> {
                    val wearable = intent.parcelableExtra<Wearable>(Wearable.EXTRA_DEVICE)!!

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
    }

    fun getWearableByMac(mac: String): Wearable? {
        wearables.forEach {
            if (it.getAddress()?.equals(mac, ignoreCase = true) == true) return it
        }

        return null
    }

    fun getWearables(): List<Wearable> = Collections.unmodifiableList(wearables)

    fun initializeInjection(wearableComponent: WearableComponent) {
        wearableComponent.inject(this)
    }

    private fun notifyWearablesChanged() {
        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(ACTION_DEVICES_CHANGED))
    }

    fun refreshPairedWearables() {
        val availableWearables = WearableHelper.getInstance().getAvailableWearables()
        wearables.retainAll(availableWearables)

        availableWearables.forEach {
            if (!wearables.contains(it)) wearables.add(it)
        }

        App.database.userDao().getLoggedIn()?.let { user ->
            wearables.firstOrNull()?.let { wearable ->
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        if (!user.isAdmin()) {
                            println("associating: $wearable to $user")

                            wearableService.associate(user.id, AssociateWearableRequest(
                                device_mac = wearable.getAddress()!!,
                                oper_system = "Android",
                                oper_system_version = Build.VERSION.RELEASE,
                                phone_brand = Build.BRAND,
                                phone_model = Build.MODEL
                            ))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        notifyWearablesChanged()
    }

    private fun storeWearable(wearable: Wearable) {
        val user = App.database.userDao().getLoggedIn() ?: return

        val model = WearableModel()
        model.firmwareVersion = wearable.getFirmwareVersion()
        model.mac = wearable.getAddress()!!
        model.name = wearable.getName()!!
        model.typeName = wearable.getType().name
        model.userId = user.id

        App.database.wearableDao().store(model)
    }

    companion object {
        const val ACTION_DEVICES_CHANGED = "com.info.logifit.pe.action.devices_changed"
    }
}