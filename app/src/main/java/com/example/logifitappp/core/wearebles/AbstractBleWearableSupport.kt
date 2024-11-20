package com.example.logifitappp.core.wearebles

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import androidx.annotation.RequiresPermission
import com.example.logifitappp.core.bluetooth.BleQueue
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.builders.ble.actions.CheckInitializedAction
import com.example.logifitappp.core.handlers.BluetoothGattCallbackHandler
import com.example.logifitappp.core.handlers.BluetoothGattServerCallbackHandler
import okio.IOException
import java.util.UUID

abstract class AbstractBleWearableSupport: AbstractWearableSupport(), BluetoothGattCallbackHandler, BluetoothGattServerCallbackHandler {
    private var availableCharacteristics: Map<UUID, BluetoothGattCharacteristic>? = null
    private val characteristicsMonitor = Object()
    private var mtu = 23
    private var queue: BleQueue? = null
    private val supportedProfile = mutableListOf<AbstractBleProfile<*>>()
    private val supportedServices = HashSet<UUID>(4)
    private val supportedServerServices = HashSet<BluetoothGattService>(4)

    protected fun addSupportedProfile(profile: AbstractBleProfile<*>) {
        supportedProfile.add(profile)
    }

    protected fun addSupportedService(service: UUID) {
        supportedServices.add(service)
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    override fun connect(): Boolean {
        if (queue == null) {
            queue = BleQueue(getBluetoothAdapter()!!, getWearable(), this, this, getContext(), supportedServerServices)
            queue!!.setAutoReconnect(getAutoReconnect())
            queue!!.setScanReconnect(getScanReconnect())
            queue!!.setImplicitGattCallbackModify(getImplicitCallbackModify())
            queue!!.setSendWriteRequestResponse(getSendWriteRequestResponse())
        }

        return queue!!.connect()
    }

    fun createTransactionBuilder(taskName: String) = TransactionBuilder(taskName)

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    open fun disconnect() {
        queue?.disconnect()
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    override fun dispose() {
        queue?.dispose()
        queue = null
    }

    private fun gattServicesDiscovered(discoveredGattServices: List<BluetoothGattService>) {
        val supportedServices = getSupportedServices()
        val newCharacteristics = HashMap<UUID, BluetoothGattCharacteristic>()

        for (service in discoveredGattServices) {
            if (supportedServices.contains(service.uuid)) {
                println("discovered supported service: ${service.uuid}")

                if (service.characteristics.isEmpty()) {
                    println("Supported LE service ${service.uuid} did not return any characteristics")
                    continue
                }

                val availableCharacteristics = HashMap<UUID, BluetoothGattCharacteristic>(service.characteristics.size)

                service.characteristics.forEach { characteristic ->
                    availableCharacteristics[characteristic.uuid] = characteristic
                    println("characteristic: ${characteristic.uuid}")
                }

                newCharacteristics.putAll(availableCharacteristics)

                synchronized(characteristicsMonitor) {
                    this.availableCharacteristics = newCharacteristics
                }
            } else println("discovered unsupported service: ${service.uuid}")
        }
    }

    fun getCharacteristic(uuid: UUID?): BluetoothGattCharacteristic? {
        if (uuid == null) return null

        synchronized(characteristicsMonitor) {
            return availableCharacteristics?.get(uuid)
        }
    }

    fun getImplicitCallbackModify() = false

    fun getQueue() = queue

    fun getSendWriteRequestResponse() = true

    protected open fun getSupportedServices(): Set<UUID> = supportedServices

    protected open fun initializeDevice(builder: TransactionBuilder) = builder

    override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic): Boolean {
        supportedProfile.forEach {
            if (it.onCharacteristicChanged(gatt, characteristic)) return true
        }

        return false
    }

    override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int): Boolean {
        supportedProfile.forEach {
            if (it.onCharacteristicRead(gatt, characteristic, status)) return true
        }

        return false
    }

    override fun onCharacteristicReadRequest(device: BluetoothDevice, requestId: Int, offset: Int, characteristic: BluetoothGattCharacteristic) = false

    override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int): Boolean {
        supportedProfile.forEach {
            if (it.onCharacteristicWrite(gatt, characteristic, status)) return true
        }

        return false
    }

    override fun onCharacteristicWriteRequest(
        device: BluetoothDevice,
        requestId: Int,
        characteristic: BluetoothGattCharacteristic,
        preparedWrite: Boolean,
        responseNeeded: Boolean,
        offset: Int,
        value: ByteArray
    ) = false

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        supportedProfile.forEach {
            it.onConnectionStateChange(gatt, status, newState)
        }
    }

    override fun onConnectionStateChange(device: BluetoothDevice, status: Int, newState: Int) {

    }

    override fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int): Boolean {
        supportedProfile.forEach {
            if (it.onDescriptorRead(gatt, descriptor, status)) return true
        }

        return false
    }

    override fun onDescriptorReadRequest(device: BluetoothDevice, requestId: Int, offset: Int, descriptor: BluetoothGattDescriptor) = false

    override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int): Boolean {
        supportedProfile.forEach {
            if (it.onDescriptorWrite(gatt, descriptor, status)) return true
        }

        return false
    }

    override fun onDescriptorWriteRequest(
        device: BluetoothDevice,
        requestId: Int,
        descriptor: BluetoothGattDescriptor,
        preparedWrite: Boolean,
        responseNeeded: Boolean,
        offset: Int,
        value: ByteArray
    ) = false

    override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
        this.mtu = mtu
    }

    override fun onReadRemoteRssi(gatt: BluetoothGatt, rssi: Int, status: Int) {
        supportedProfile.forEach {
            it.onReadRemoteRssi(gatt, rssi, status)
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt) {
        gattServicesDiscovered(gatt.services)

        if (getWearable().getState() >= Wearable.State.INITIALIZING) {
            println("Services discovered, but device state is already ${getWearable().getState()} for device: ${getWearable()}, so ignoring")
            return
        }

        initializeDevice(createTransactionBuilder("Initializing device")).queue(queue!!)
    }

    fun performImmediately(builder: TransactionBuilder) {
        if (!isConnected()) throw IOException("Not connected to device: ${getWearable()}")

        getQueue()?.insert(builder.transaction)
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    fun performInitialized(taskName: String): TransactionBuilder {
        if (!isConnected()) {
            println("Connecting to device for $taskName")

            if (!connect()) throw IOException("1: Unable to connect to device ${getWearable()}")
        }

        if (!isInitialized()) {
            println("Initializing device for $taskName")

            val builder = createTransactionBuilder("Initialize device")
            builder.add(CheckInitializedAction(getWearable()))
            initializeDevice(builder).queue(queue!!)
        }

        return createTransactionBuilder(taskName)
    }

    companion object {
        const val BASE_UUID = "0000%s-0000-1000-8000-00805f9b34fb"
    }
}