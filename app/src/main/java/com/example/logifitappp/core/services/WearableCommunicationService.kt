package com.example.logifitappp.core.services

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.example.logifitappp.core.App
import com.example.logifitappp.core.AppPreferences
import com.example.logifitappp.core.broadcasters.PhoneCallBroadcastReceiver
import com.example.logifitappp.core.utils.parcelableExtra
import com.example.logifitappp.exceptions.WearableNotFoundException
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableCoordinator
import com.example.logifitappp.core.wearebles.WearableService
import com.example.logifitappp.core.wearebles.WearableSupport
import com.example.logifitappp.core.wearebles.WearableSupportFactory
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.SynchronizationProcessingException
import java.util.Collections

class WearableCommunicationService: Service(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val cacheNotifications = hashMapOf<String, MutableList<Intent>>()
    private var phoneCallReceiver: PhoneCallBroadcastReceiver? = null
    private val structs = ArrayList<WearableStruct>(1)

    private fun connect(wearable: Wearable?, firstTime: Boolean) {
        val wearables = arrayListOf<Wearable>()
        var fromExtra = false

        if (wearable != null) {
            if (!wearable.getWearableCoordinator().isConnectable()) {
                println("Cannot connect to Scannable Device")
                return
            }

            wearables.add(wearable)
            fromExtra = true
        } else {
            val storedWearables = App.wearableManager.getWearables()

            if (storedWearables.isNotEmpty()) {
                if (App.preferences.getBoolean(AppPreferences.RECONNECT_ONLY_TO_CONNECTED, true)) {
                    val lastWearableAddresses = App.preferences.getStringSet(AppPreferences.LAST_DEVICE_ADDRESSES, Collections.emptySet())

                    if (lastWearableAddresses.isNotEmpty()) {
                        for (storedWearable in storedWearables) {
                            if (lastWearableAddresses.contains(storedWearable.getAddress())) wearables.add(storedWearable)
                        }
                    }
                } else wearables.addAll(storedWearables)
            }
        }

        if (wearables.isEmpty()) return

        for (mWearable in wearables) {
            if (!mWearable.getWearableCoordinator().isConnectable()) continue

            val address = mWearable.getAddress()
            val autoReconnect = App.preferences.getAutoReconnect(mWearable)

            if (!fromExtra && !autoReconnect) continue

            val lastWearableAddresses = HashSet(App.preferences.getStringSet(AppPreferences.LAST_DEVICE_ADDRESSES, Collections.emptySet()))

            if (!lastWearableAddresses.contains(address)) {
                lastWearableAddresses.add(address!!)
                App.preferences.getPreferences().edit().putStringSet(AppPreferences.LAST_DEVICE_ADDRESSES, lastWearableAddresses).apply()
            }

            var registeredStructure = getWearableStructOrNull(mWearable)

            if (registeredStructure == null) {
                registeredStructure = createDeviceStruct(mWearable)
            } else {
                val wearableFromStruct = registeredStructure.wearable

                if (isWearableConnecting(wearableFromStruct) || isWearableConnected(wearableFromStruct)) continue

                try {
                    removeWearableSupport(mWearable)
                } catch (e: WearableNotFoundException) {
                    println("connectToDevice(): Failed to remove device support: $e")
                }
            }

            try {
                val support = WearableSupportFactory(App.context).createWearableSupport(mWearable)

                if (support != null) {
                    setWearableSupport(mWearable, support)

                    if (firstTime) support.connectFirstTime()
                    else {
                        support.setAutoReconnect(autoReconnect)
                        support.setScanReconnect(App.preferences.getAutoReconnectByScan())
                        support.connect()
                    }
                } else {
                    println("Can't create wearable support")

                    App.signalFailedConnectionWithWearable(AppStatusCodeEnum.UNSUPPORTED_WEARABLE)
                }
            } catch (e: Exception) {
                println("Cannot connect: ${e.message}")

                if (e is SynchronizationProcessingException) App.signalFailedConnectionWithWearable(e.getStatus())
                else App.signalFailedConnectionWithWearable(AppStatusCodeEnum.FAILED_WEARABLE_PAIRING)
            }

            registeredStructure.wearable?.sendDeviceUpdateIntent(App.context)
        }
    }

    private fun createDeviceStruct(wearable: Wearable): WearableStruct {
        val struct = WearableStruct()
        struct.wearable = wearable
        struct.coordinator = wearable.getWearableCoordinator()
        structs.add(struct)

        return struct
    }

    fun disconnect(wearable: Wearable, context: Context) {
        try {
            removeWearableSupport(wearable)
        } catch (e: WearableNotFoundException) {
            println("Trying to disconnect unknown device: $e")
        }

        wearable.setState(Wearable.State.NOT_CONNECTED)
        wearable.sendDeviceUpdateIntent(context)
        updateReceiversState()
    }

    private fun handleAction(intent: Intent, action: String?, wearable: Wearable) {
        if (WearableService.ACTION_DISCONNECT == action) {
            disconnect(wearable, this)
            return
        }

        val support = getWearableSupport(wearable)

        when (action) {
            WearableService.ACTION_FETCH_RECORDED_DATA -> {
                val dataTypes = intent.getIntExtra(WearableService.EXTRA_RECORDED_DATA_TYPES, 0)
                support.onFetchRecordedData(dataTypes)
            }
        }
    }

    private fun getWearableByAddress(address: String?): Wearable {
        if (address == null) throw WearableNotFoundException(null)

        structs.forEach {
            if (it.wearable?.getAddress() === address) return it.wearable!!
        }

        throw WearableNotFoundException(address)
    }

    private fun getWearableByAddressOrNull(address: String?): Wearable? {
        return try {
            getWearableByAddress(address)
        } catch (e: WearableNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    private fun getWearables(): List<Wearable> {
        val wearables = mutableListOf<Wearable>()

        structs.forEach {
            wearables.add(it.wearable!!)
        }

        return wearables.toList()
    }

    private fun getWearableStruct(wearable: Wearable?): WearableStruct {
        if (wearable == null) throw WearableNotFoundException("null")

        for (struct in structs) {
            if (struct.wearable?.equals(wearable) == true) return struct
        }

        throw WearableNotFoundException(wearable)
    }

    private fun getWearableStructOrNull(wearable: Wearable?): WearableStruct? {
        try {
            return getWearableStruct(wearable)
        } catch (e: WearableNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    private fun getWearableSupport(wearable: Wearable?): WearableSupport {
        if (wearable == null) throw WearableNotFoundException("null")

        for (struct in structs) {
            if (struct.wearable?.equals(wearable) == true) {
                if (struct.support == null) throw WearableNotFoundException(wearable)

                return struct.support!!
            }
        }

        throw WearableNotFoundException(wearable)
    }

    private fun isWearableConnected(wearableAddress: String?): Boolean {
        if (wearableAddress == null) return false

        for (struct in structs) {
            if (struct.wearable?.getAddress()?.compareTo(wearableAddress, true) == 0) {
                return struct.wearable!!.isConnected()
            }
        }

        return false
    }

    private fun isWearableConnected(wearable: Wearable?): Boolean {
        return isWearableConnected(wearable?.getAddress())
    }

    private fun isWearableConnecting(wearableAddress: String?): Boolean {
        if (wearableAddress == null) return false

        for (struct in structs) {
            if (struct.wearable?.getAddress()?.compareTo(wearableAddress, true) == 0) {
                return struct.wearable!!.isConnecting()
            }
        }

        return false
    }

    private fun isWearableConnecting(wearable: Wearable?): Boolean {
        return isWearableConnecting(wearable?.getAddress())
    }

    private fun isWearableInitialized(wearable: Wearable): Boolean {
        return isWearableInitialized(wearable.getAddress()!!)
    }

    private fun isWearableInitialized(macAddress: String): Boolean {
        structs.forEach {
            if (it.wearable!!.getAddress()!!.compareTo(macAddress, true) == 0) {
                return it.wearable!!.isInitialized()
            }
        }

        return false
    }

    private fun isWearableReconnecting(wearable: Wearable?): Boolean {
        val w = getWearableByAddressOrNull(wearable?.getAddress())

        if (w != null) return w.getState().equalsOrHigherThan(Wearable.State.NOT_CONNECTED)

        return false
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            AppPreferences.DEVICE_AUTO_RECONNECT -> {
                structs.forEach {
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

        val wearable = intent.parcelableExtra<Wearable>(Wearable.EXTRA_DEVICE)

        when (intent.action) {
            WearableService.ACTION_CONNECT -> {
                val firstTime = intent.getBooleanExtra(WearableService.EXTRA_CONNECT_FIRST_TIME, false)
                connect(wearable, firstTime)
            }

            else -> {
                val action = intent.action
                val wearables = mutableListOf<Wearable>()

                if (wearable != null) wearables.add(wearable)
                else {
                    getWearables().forEach {
                        when {
                            isWearableInitialized(it) -> wearables.add(it)

                            isWearableReconnecting(it) && action == WearableService.ACTION_NOTIFICATION && App.preferences.getBoolean("notification_cache_while_disconnected", false) -> {
                                if (!cacheNotifications.containsKey(it.getAddress())) cacheNotifications[it.getAddress()!!] = mutableListOf()

                                val cache = cacheNotifications[it.getAddress()]!!
                                cache.add(intent)

                                if (cache.size > NOTIFICATIONS_CACHE_MAX) cache.removeAt(0)
                            }

                            action == WearableService.ACTION_DELETE_NOTIFICATION -> {
                                cacheNotifications[it.getAddress()]?.let { cache ->
                                    val id = intent.getIntExtra(WearableService.EXTRA_NOTIFICATION_ID, -1)
                                    val toRemove = mutableListOf<Intent>()

                                    cache.forEach { i ->
                                        if (id == i.getIntExtra(WearableService.EXTRA_NOTIFICATION_ID, -1)) toRemove.add(i)
                                    }

                                    cache.removeAll(toRemove)
                                }
                            }

                            action == WearableService.ACTION_DISCONNECT && it.getState() != Wearable.State.NOT_CONNECTED -> wearables.add(it)
                        }
                    }
                }

                wearables.forEach {
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

    private fun removeWearableSupport(wearable: Wearable) {
        val struct = getWearableStruct(wearable)
        struct.support?.dispose()
        struct.support = null
    }

    private fun setReceiversEnableState(enable: Boolean) {
        if (enable) {
            if (phoneCallReceiver == null) {
                phoneCallReceiver = PhoneCallBroadcastReceiver()

                IntentFilter().apply {
                    addAction("android.intent.action.PHONE_STATE")
                    addAction("android.intent.action.NEW_OUTGOING_CALL")
                    addAction("com.info.logifit.pe.MUTE_CALL")
                    ContextCompat.registerReceiver(this@WearableCommunicationService, phoneCallReceiver, this, ContextCompat.RECEIVER_EXPORTED)
                }
            }
        }
    }

    private fun setWearableSupport(wearable: Wearable, wearableSupport: WearableSupport) {
        val struct  = getWearableStruct(wearable)
        val cachedWearableSupport = struct.support

        if (wearableSupport != cachedWearableSupport && cachedWearableSupport != null) cachedWearableSupport.dispose()

        struct.support = wearableSupport
    }

    private fun updateReceiversState() {
        var enableReceivers = false

        structs.forEach {
            val support = it.support

            if (support != null && support.useAutoConnect() || isWearableInitialized(
                    it.wearable!!
                )
            ) enableReceivers = true
        }

        setReceiversEnableState(enableReceivers)
    }

    companion object {
        private const val NOTIFICATIONS_CACHE_MAX = 10

        fun isRunning(context: Context): Boolean {
            val manager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager? ?: return false

            manager.getRunningServices(Integer.MAX_VALUE).forEach {
                if (WearableCommunicationService::class.java.name.equals(it.service.className)) return true
            }

            return false
        }
    }

    class WearableStruct {
        var coordinator: WearableCoordinator? = null
        var wearable: Wearable? = null
        var support: WearableSupport? = null
    }
}