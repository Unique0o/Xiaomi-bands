package com.example.logifitappp.core.wearebles

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.location.LocationManager
import com.example.logifitappp.core.App
import com.example.logifitappp.core.services.WearableSupportService
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.SynchronizationProcessingException

class WearableSupportFactory(private val context: Context) {
    private val bluetoothAdapter: BluetoothAdapter?
    private val locationManager: LocationManager?

    init {
        val manager = App.context.getSystemService(BluetoothManager::class.java)

        bluetoothAdapter = manager.adapter
        locationManager = App.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
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

        checkLocationAvailability()
        checkBluetoothAvailability()

        return wearableSupport
    }

    private fun checkBluetoothAvailability() {
        if (bluetoothAdapter == null) {
            println("Bluetooth not supported")
            throw SynchronizationProcessingException(AppStatusCodeEnum.FAILED_WEARABLE_PAIRING)
        } else if (!bluetoothAdapter.isEnabled) {
            println("Bluetooth is disabled")
            throw SynchronizationProcessingException(AppStatusCodeEnum.DISABLED_BLUETOOTH)
        }
    }

    private fun checkLocationAvailability() {
        if (locationManager == null) {
            println("Location not supported")
            throw SynchronizationProcessingException(AppStatusCodeEnum.FAILED_WEARABLE_PAIRING)
        } else {
            val isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isEnabled) {
                println("Location is disabled")
                throw SynchronizationProcessingException(AppStatusCodeEnum.DISABLED_LOCATION)
            }
        }
    }

    private fun createBluetoothWearableSupport(wearable: Wearable): WearableSupport? {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) return null

        try {
            val support = createServiceWearableSupport(wearable)
            support.setContext(wearable, bluetoothAdapter, context)

            return support
        } catch (e: Exception) {
            throw Exception("Cannot connect. Bluetooth address invalid?")
        }
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
            println("Ignoring exception: $e")
        } catch (e: ReflectiveOperationException) {
            println("error calling DeviceSupport constructor with argument 'DeviceType'")
            throw Exception(e)
        }

        try {
            val supportInstance = supportClass.newInstance() as WearableSupport
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