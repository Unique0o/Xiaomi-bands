package com.example.logifitappp.core.builders.ble.profiles

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Intent
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.builders.ble.profiles.parcelables.WearableInfo
import com.example.logifitappp.core.utils.GattCharacteristic
import com.example.logifitappp.core.wearebles.AbstractBleWearableSupport

class WearableInfoProfile<T: AbstractBleWearableSupport>(support: T): AbstractBleProfile<T>(support) {
    private val wearableInfo = WearableInfo()

    private fun createIntent(wearableInfo: WearableInfo): Intent {
        return Intent(ACTION_DEVICE_INFO).apply {
            putExtra(EXTRA_DEVICE_INFO, wearableInfo)
        }
    }

    private fun handleFirmwareRevision(characteristic: BluetoothGattCharacteristic) {
        val firmware = characteristic.getStringValue(0).trim()
        wearableInfo.firmwareRevision = firmware
        notify(createIntent(wearableInfo))
    }

    private fun handleHardwareRevision(characteristic: BluetoothGattCharacteristic) {
        val hardware = characteristic.getStringValue(0).trim()
        wearableInfo.hardwareRevision = hardware
        notify(createIntent(wearableInfo))
    }

    private fun handleSoftwareRevision(characteristic: BluetoothGattCharacteristic) {
        val software = characteristic.getStringValue(0).trim()
        wearableInfo.softwareRevision = software
        notify(createIntent(wearableInfo))
    }

    override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int): Boolean {
        if (status != BluetoothGatt.GATT_SUCCESS) return false

        when (characteristic.uuid) {
            GattCharacteristic.UUID_CHARACTERISTIC_HARDWARE_REVISION_STRING -> {
                handleHardwareRevision(characteristic)
                return true
            }
            GattCharacteristic.UUID_CHARACTERISTIC_FIRMWARE_REVISION_STRING -> {
                handleFirmwareRevision(characteristic)
                return true
            }
            GattCharacteristic.UUID_CHARACTERISTIC_SOFTWARE_REVISION_STRING -> {
                handleSoftwareRevision(characteristic)
                return true
            }
        }

        return false
    }

    fun requestDeviceInfo(builder: TransactionBuilder) {
        builder.read(getCharacteristic(GattCharacteristic.UUID_CHARACTERISTIC_HARDWARE_REVISION_STRING))
        builder.read(getCharacteristic(GattCharacteristic.UUID_CHARACTERISTIC_FIRMWARE_REVISION_STRING))
        builder.read(getCharacteristic(GattCharacteristic.UUID_CHARACTERISTIC_SOFTWARE_REVISION_STRING))
    }

    companion object {
        val ACTION_DEVICE_INFO = "${WearableInfoProfile::class.simpleName}_DEVICE_INFO"
        const val EXTRA_DEVICE_INFO = "DEVICE_INFO"
    }
}