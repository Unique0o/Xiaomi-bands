package com.example.logifitappp.core.bluetooth

import android.content.Context
import com.example.logifitappp.core.App
import com.example.logifitappp.core.AppPreferences
import com.example.logifitappp.exceptions.WearableNotFoundException
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableCoordinator
import com.example.logifitappp.core.wearebles.WearableService
import com.example.logifitappp.core.wearebles.WearableSupport
import com.example.logifitappp.core.wearebles.WearableSupportFactory
import java.util.Collections

object BluetoothConnector {
    private val structs = ArrayList<WearableStruct>(1)

    fun connect(wearable: Wearable?, firstTime: Boolean) {
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
                    // TODO WEARABLE SUPPORT NOT SUPPORTED
                }
            } catch (e: Exception) {
                println("Cannot connect: ${e.message}")
                //TODO CANNOT CONNECT
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
    }

    fun getStructs() = structs

    fun getTargetedWearables(action: String?, wearable: Wearable?): MutableList<Wearable> {
        val wearables = mutableListOf<Wearable>()

        if (wearable != null) wearables.add(wearable)
        else {
            getWearables().forEach {
                if (isWearableInitialized(it)) wearables.add(it)
                else if (action == WearableService.ACTION_DISCONNECT && it.getState() != Wearable.State.NOT_CONNECTED) {
                    wearables.add(it)
                }
            }
        }

        return wearables
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

    fun getWearableSupport(wearable: Wearable?): WearableSupport {
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

    private fun removeWearableSupport(wearable: Wearable) {
        val struct = getWearableStruct(wearable)
        struct.support?.dispose()
        struct.support = null
    }

    private fun setWearableSupport(wearable: Wearable, wearableSupport: WearableSupport) {
        val struct  = getWearableStruct(wearable)
        val cachedWearableSupport = struct.support

        if (wearableSupport != cachedWearableSupport && cachedWearableSupport != null) cachedWearableSupport.dispose()

        struct.support = wearableSupport
    }

    class WearableStruct {
        var coordinator: WearableCoordinator? = null
        var wearable: Wearable? = null
        var support: WearableSupport? = null
    }
}