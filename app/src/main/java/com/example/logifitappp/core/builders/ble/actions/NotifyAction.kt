package com.example.logifitappp.core.builders.ble.actions

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothStatusCodes
import android.os.Build
import androidx.annotation.RequiresPermission
import com.example.logifitappp.core.wearebles.AbstractBleWearableSupport
import java.util.UUID

class NotifyAction(characteristic: BluetoothGattCharacteristic?, private val enable: Boolean) : Action(characteristic) {
    private var hasWrittenDescriptor = false

    override fun expectsResult() = hasWrittenDescriptor

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    override fun run(gatt: BluetoothGatt?): Boolean {
        if (gatt == null) return false.also { println("gatt == null") }

        var result = gatt.setCharacteristicNotification(getCharacteristic(), enable)

        if (result) {
            val clientGattDescriptor = getCharacteristic()!!.getDescriptor(UUID.fromString((String.format(AbstractBleWearableSupport.BASE_UUID, "2902"))))

            if (clientGattDescriptor != null) {
                val properties = getCharacteristic()!!.properties

                if ((properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    println("use NOTIFICATION for Characteristic ${getCharacteristic()?.uuid}")

                    val payload = if (enable) BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
                    result = writeDescriptor(gatt, clientGattDescriptor, payload)
                    hasWrittenDescriptor = true
                } else if ((properties and BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
                    println("use INDICATION for Characteristic ${getCharacteristic()?.uuid}")

                    val payload = if (enable) BluetoothGattDescriptor.ENABLE_INDICATION_VALUE else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
                    result = writeDescriptor(gatt, clientGattDescriptor, payload)
                    hasWrittenDescriptor = true
                } else {
                    println("use neither NOTIFICATION nor INDICATION for Characteristic ${getCharacteristic()?.uuid}")
                    hasWrittenDescriptor = false
                }
            } else {
                println("Descriptor CLIENT_CHARACTERISTIC_CONFIGURATION for characteristic ${getCharacteristic()?.uuid} is null")
                hasWrittenDescriptor = false
            }
        } else {
            println("Unable to enable notifications for ${getCharacteristic()?.uuid}")
            hasWrittenDescriptor = false
        }

        return result
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    private fun writeDescriptor(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, payload: ByteArray): Boolean {
        if (gatt == null) return false.also { println("gatt == null") }

        if (descriptor == null) return false.also { println("descriptor == null") }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            try {
                val result = gatt.writeDescriptor(descriptor, payload)

                if (result != BluetoothStatusCodes.SUCCESS) {
                    return false.also { println("Writing characteristic ${descriptor.characteristic.uuid} descriptor failed: $result") }
                }
            } catch (e: SecurityException) {
                return false.also { println("SecurityException while writing to characteristic ${descriptor.characteristic.uuid} descriptor: ${e.message}") }
            }
        } else {
            if (!descriptor.setValue(payload)) {
                return false.also { println("Updating descriptor value on characteristic ${descriptor.characteristic.uuid} failed") }
            }

            if (!gatt.writeDescriptor(descriptor)) {
                return false.also { println("Writing descriptor on characteristic ${descriptor.characteristic.uuid} failed") }
            }
        }

        return true.also { println("Successfully written characteristic ${descriptor.characteristic.uuid} descriptor") }
    }
}