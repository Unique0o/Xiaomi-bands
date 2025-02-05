package com.example.logifitappp.core.handlers

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor

interface BluetoothGattCallbackHandler {
    fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic): Boolean
    fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int): Boolean
    fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int): Boolean
    fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int)
    fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int): Boolean
    fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int): Boolean
    fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int)
    fun onReadRemoteRssi(gatt: BluetoothGatt, rssi: Int, status: Int)
    fun onServicesDiscovered(gatt: BluetoothGatt)
}