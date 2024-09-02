package com.example.logifitappp.core.wearebles

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.example.logifitappp.core.App
import com.example.logifitappp.core.services.WearableSupportService

class WearableSupportFactory(private val context: Context) {
    private var adapter: BluetoothAdapter? = null

    init {
        val manager = App.context.getSystemService(BluetoothManager::class.java)
        adapter = manager.adapter
    }

    @Synchronized
    fun createWearableSupport(wearable: Wearable): WearableSupport? {
        val deviceAddress = wearable.getAddress()
        val indexFirstColon = deviceAddress!!.indexOf(":")

        val wearableSupport: WearableSupport? = when {
            indexFirstColon > 0 && indexFirstColon == deviceAddress.lastIndexOf(":") -> createTCPWearableSupport(wearable)
            indexFirstColon > 0 -> createBluetoothWearableSupport(wearable)
            else ->  createClassNameWearableSupport(wearable)
        }

        return wearableSupport ?: run {
            checkBluetoothAvailability()
            return null
        }
    }

    private fun checkBluetoothAvailability() {
        if (adapter == null) {
            println("Bluetooth not supported")
            // TODO BLUETOOTH NOT SUPPORTED
        } else if (!adapter!!.isEnabled) {
            println("Bluetooth is disabled")
            // TODO BLUETOOTH DISABLED
        }
    }

    private fun createBluetoothWearableSupport(wearable: Wearable): WearableSupport? {
        if (adapter == null || !adapter!!.isEnabled) return null

        try {
            val support = createServiceWearableSupport(wearable)
            support.setContext(wearable, adapter!!, context)

            return support
        } catch (e: Exception) {
            throw Exception("Cannot connect. Bluetooth address invalid?")
        }

        return null
    }

    private fun createClassNameWearableSupport(wearable: Wearable): WearableSupport? {
        val className = wearable.getAddress()

        try {
            val wearableSupportClass = className?.let { Class.forName(it) }
            val constructor = wearableSupportClass?.getConstructor()

            val support = constructor?.newInstance() as WearableSupport
            support.setContext(wearable, null, context)

            return support
        } catch (e: ClassNotFoundException) {
            return null
        } catch (e: Exception) {
            throw Exception("Error creating DeviceSupport instance for $className")
        }
    }

    private fun createServiceWearableSupport(wearable: Wearable): WearableSupportService {
        val coordinator = wearable.getWearableCoordinator()
        val supportClass = coordinator.getWearableSupportClass()

        try {
            val supportConstructor = supportClass.getConstructor(WearableTypeEnum::class.java)
            val supportInstance = supportConstructor.newInstance(wearable.getWearableType()) as WearableSupport

            return WearableSupportService(supportInstance, coordinator.getInitialFlags())
        } catch (e: NoSuchMethodException) {
            // ignore
        } catch (e: ReflectiveOperationException) {
            println("error calling DeviceSupport constructor with argument 'DeviceType'")
            throw Exception(e)
        }

        try {
            val supportInstance = supportClass.getDeclaredConstructor().newInstance() as WearableSupport
            return WearableSupportService(supportInstance, coordinator.getInitialFlags())
        } catch (e: ReflectiveOperationException) {
            println("error calling DeviceSupport constructor with zero arguments")
            throw Exception(e)
        }
    }

    private fun createTCPWearableSupport(wearable: Wearable): WearableSupport? {
        //TODO WHEN PEBBLE IS REQUIRED
        return null
    }
}