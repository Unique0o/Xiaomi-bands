package com.example.logifitappp.core.builders.ble.profiles

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Intent
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.builders.ble.profiles.parcelables.WearableBatteryInfo
import com.example.logifitappp.core.utils.GattCharacteristic
import com.example.logifitappp.core.wearebles.AbstractBleWearableSupport

class WearableBatteryInfoProfile<T: AbstractBleWearableSupport>(support: T): AbstractBleProfile<T>(support) {
    private val batteryInfo = WearableBatteryInfo()

    private fun createIntent(batteryInfo: WearableBatteryInfo): Intent {
        return Intent(ACTION_BATTERY_INFO).apply {
            putExtra(EXTRA_BATTERY_INFO, batteryInfo)
        }
    }

    override fun enableNotify(builder: TransactionBuilder, enable: Boolean) {
        builder.notify(getCharacteristic(GattCharacteristic.UUID_CHARACTERISTIC_BATTERY_LEVEL), enable)
    }

    private fun handleBatteryLevel(characteristic: BluetoothGattCharacteristic) {
        val percent = CharacteristicValueDecoder.decodePercent(characteristic)
        batteryInfo.percentCharged = percent

        notify(createIntent(batteryInfo))
    }

    override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int): Boolean {
        when (status) {
            BluetoothGatt.GATT_SUCCESS -> {
                if (characteristic.uuid.equals(GattCharacteristic.UUID_CHARACTERISTIC_BATTERY_LEVEL)) {
                    handleBatteryLevel(characteristic)
                    return true
                }

                println("Unexpected onCharacteristicRead: $characteristic")
            }

            else -> println("error reading from characteristic: $characteristic")
        }

        return false
    }

    fun requestBatteryInfo(builder: TransactionBuilder) {
        builder.read(getCharacteristic(GattCharacteristic.UUID_CHARACTERISTIC_BATTERY_LEVEL))
    }

    companion object {
        val ACTION_BATTERY_INFO = "${WearableInfoProfile::class.simpleName}_BATTERY_INFO"
        const val EXTRA_BATTERY_INFO = "BATTERY_INFO"
    }
}