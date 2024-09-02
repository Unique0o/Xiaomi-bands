package com.example.logifitappp.core.handlers

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor

abstract class AbstractBluetoothGattCallbackHandler: BluetoothGattCallbackHandler {
    override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) = false

    override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) = false

    override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) = false

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {

    }

    override fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) = false

    override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) = false

    override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {

    }

    override fun onReadRemoteRssi(gatt: BluetoothGatt, rssi: Int, status: Int) {

    }

    override fun onServicesDiscovered(gatt: BluetoothGatt) {

    }
}