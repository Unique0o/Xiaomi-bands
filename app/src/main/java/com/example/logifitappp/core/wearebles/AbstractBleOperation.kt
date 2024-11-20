package com.example.logifitappp.core.wearebles

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.Context
import androidx.annotation.RequiresPermission
import com.example.logifitappp.core.bluetooth.BleQueue
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.handlers.BluetoothGattCallbackHandler
import com.example.logifitappp.enums.OperationStatusEnum
import java.util.UUID

abstract class AbstractBleOperation<T: AbstractBleWearableSupport>(
    private val support: T
): BluetoothGattCallbackHandler, BleOperation {
    protected val context: Context
        get() = support.getContext()

    val isOperationRunning: Boolean
        get() = operationStatus == OperationStatusEnum.RUNNING

    val isOperationFinished: Boolean
        get() = operationStatus == OperationStatusEnum.FINISHED

    protected val queue: BleQueue?
        get() = support.getQueue()

    protected val wearable: Wearable
        get() = support.getWearable()

    private var name: String? = null
    protected var operationStatus = OperationStatusEnum.INITIAL

    fun createTransactionBuilder(taskName: String): TransactionBuilder {
        return support.createTransactionBuilder(taskName).apply {
            setCallback(this@AbstractBleOperation)
        }
    }

    protected fun getCharacteristic(uuid: UUID): BluetoothGattCharacteristic? {
        return support.getCharacteristic(uuid)
    }

    override fun getName(): String {
        return name ?: wearable.getBusyTask() ?: javaClass.simpleName
    }

    fun getSupport() = support

    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ): Boolean {
        return support.onCharacteristicRead(gatt, characteristic, status)
    }

    override fun onCharacteristicWrite(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ): Boolean {
        return support.onCharacteristicWrite(gatt, characteristic, status)
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ): Boolean {
        return support.onCharacteristicChanged(gatt, characteristic)
    }

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        support.onConnectionStateChange(gatt, status, newState)
    }

    override fun onDescriptorRead(
        gatt: BluetoothGatt,
        descriptor: BluetoothGattDescriptor,
        status: Int
    ): Boolean {
        return support.onDescriptorRead(gatt, descriptor, status)
    }

    override fun onDescriptorWrite(
        gatt: BluetoothGatt,
        descriptor: BluetoothGattDescriptor,
        status: Int
    ): Boolean {
        return support.onDescriptorWrite(gatt, descriptor, status)
    }

    override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
        support.onMtuChanged(gatt, mtu, status)
    }

    override fun onReadRemoteRssi(gatt: BluetoothGatt, rssi: Int, status: Int) {
        support.onReadRemoteRssi(gatt, rssi, status)
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt) {
        support.onServicesDiscovered(gatt)
    }

    protected open fun operationFinished() {

    }

    override fun perform() {
        operationStatus = OperationStatusEnum.STARTED
        prePerform()
        operationStatus = OperationStatusEnum.RUNNING
        doPerform()
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    open fun performInitialized(taskName: String): TransactionBuilder {
        return support.performInitialized(taskName).apply {
            setCallback(this@AbstractBleOperation)
        }
    }

    protected open fun prePerform() {

    }

    fun performImmediately(builder: TransactionBuilder) {
        support.performImmediately(builder)
    }

    protected fun setName(name: String) {
        this.name = name
    }

    protected fun unsetBusy() {
        if (wearable.isBusy()) {
            wearable.unsetBusyTask()
            wearable.sendDeviceUpdateIntent(context)
        }
    }

    protected abstract fun doPerform()
}