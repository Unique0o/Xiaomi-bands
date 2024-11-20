package com.example.logifitappp.core.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothGattServerCallback
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.example.logifitappp.core.App
import com.example.logifitappp.core.builders.AbstractTransaction
import com.example.logifitappp.core.builders.ble.ServerTransaction
import com.example.logifitappp.core.builders.ble.Transaction
import com.example.logifitappp.core.builders.ble.actions.GattListenerAction
import com.example.logifitappp.core.builders.ble.actions.WriteAction
import com.example.logifitappp.core.handlers.BluetoothGattCallbackHandler
import com.example.logifitappp.core.handlers.BluetoothGattServerCallbackHandler
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableUpdateSubjectEnum
import java.util.concurrent.CountDownLatch
import java.util.concurrent.LinkedBlockingQueue

class BleQueue(
    private val bluetoothAdapter: BluetoothAdapter,
    private val wearable: Wearable,
    bluetoothGattCallbackHandler: BluetoothGattCallbackHandler,
    bluetoothGattServerCallbackHandler: BluetoothGattServerCallbackHandler,
    private val context: Context,
    private val supportedServerServices: Set<BluetoothGattService>
) {
    private var abortServerTransaction = false
    private var abortTransaction = false
    private var autoReconnect = false
    private var bluetoothGatt: BluetoothGatt? = null
    private var bluetoothGattServer: BluetoothGattServer? = null
    private var connectionLatch: CountDownLatch? = null
    private var crashed = false
    private var disposed = false
    private val gattMonitor = Object()
    private var implicitGattCallbackModify = true
    private val internalGattCallback = InternalGattCallback(bluetoothGattCallbackHandler)
    private val internalGattServerCallback = InternalGattServerCallback(bluetoothGattServerCallbackHandler)
    private var pauseTransaction = false
    private var scanReconnect = false
    private var sendWriteRequestResponse = false
    private val transactions = LinkedBlockingQueue<AbstractTransaction>()
    private var waitCharacteristic: BluetoothGattCharacteristic? = null
    private var waitForActionResultLatch: CountDownLatch? = null
    private var waitForServerActionResultLatch: CountDownLatch? = null

    private var dispatchThread: Thread? = object: Thread("Logifit GATT Dispatcher") {
        override fun run() {
            println("Queue Dispatch Thread started.")

            while (!disposed && !crashed) {
                try {
                    val transaction = transactions.take()

                    if (!isConnected()) {
                        println("not connected, waiting for connection...")
                        internalGattCallback.reset()

                        connectionLatch = CountDownLatch(1)
                        connectionLatch!!.await()
                        connectionLatch = null
                    }

                    when (transaction) {
                        is ServerTransaction -> {
                            internalGattServerCallback.setTransactionGattServerCallback(transaction.callbackHandler)
                            abortServerTransaction = false

                            for (action in transaction.getActions()) {
                                if (abortServerTransaction) {
                                    println("Aborting running transaction")
                                    break
                                }

                                if (action.run(bluetoothGattServer)) {
                                    if (action.expectsResult()) {
                                        waitForServerActionResultLatch?.await()
                                        waitForServerActionResultLatch = null

                                        if (abortServerTransaction) break
                                    }
                                } else {
                                    println("Action returned false: $action")
                                    break
                                }
                            }
                        }

                        is Transaction -> {
                            println("Changing gatt callback for ${transaction.getTaskName()}? ${transaction.modifyCallbackHandler}")

                            if (implicitGattCallbackModify || transaction.modifyCallbackHandler) {
                                internalGattCallback.setTransactionGattCallback(transaction.callbackHandler)
                            }

                            abortTransaction = false

                            for (action in transaction.getActions()) {
                                if (abortTransaction) {
                                    println("Aborting running transaction")
                                    break
                                }

                                while ((action is WriteAction) && pauseTransaction && !abortTransaction) {
                                    println("Pausing WriteAction")

                                    try {
                                        sleep(100)
                                    } catch (e: Exception) {
                                        println("Exception during pause: $e")
                                        break
                                    }
                                }

                                waitCharacteristic = action.getCharacteristic()
                                waitForActionResultLatch = CountDownLatch(1)

                                if (action is GattListenerAction) internalGattCallback.setTransactionGattCallback(action.getGattCallback())

                                if (action.run(bluetoothGatt)) {
                                    if (action.expectsResult()) {
                                        waitForActionResultLatch?.await()
                                        waitForActionResultLatch = null

                                        if (abortTransaction) break
                                    }
                                } else {
                                    println("Action returned false: $action")
                                    break
                                }
                            }
                        }
                    }
                } catch (ignored: InterruptedException) {
                    connectionLatch = null
                    println("Thread interrupted")
                } catch (e: Throwable) {
                    println("Queue Dispatch Thread died: ${e.message}")
                    crashed = true
                    connectionLatch = null
                } finally {
                    waitForActionResultLatch = null
                    waitCharacteristic = null
                }
            }

            println("Queue Dispatch Thread terminated.")
        }
    }

    init {
        dispatchThread?.start()
    }

    fun add(transaction: Transaction) {
        if (!transaction.isEmpty()) transactions.add(transaction)
    }

    private fun checkCorrectBluetoothDevice(device: BluetoothDevice) = when (!device.address.equals(wearable.getAddress())) {
        true -> false.also { println("Ignoring request from wrong Bluetooth device: ${device.address}") }
        false -> true
    }

    private fun checkCorrectGattInstance(gatt: BluetoothGatt?, where: String) = when(gatt != bluetoothGatt && bluetoothGatt != null) {
        true -> false.also { println("Ignoring event from wrong BluetoothGatt instance: $where; $gatt") }
        false -> true
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    fun connect(): Boolean {
        pauseTransaction = false

        if (isConnected()) return false.also { println("Ignoring connect() because already connected.") }

        synchronized(gattMonitor) {
            if (bluetoothGatt != null) {
                println("connect() requested -- disconnecting previous connection: ${wearable.getName()}")
                disconnect()
            }
        }

        println("Attempting to connect to ${wearable.getName()}")
        bluetoothAdapter.cancelDiscovery()

        val remoteWearable = bluetoothAdapter.getRemoteDevice(wearable.getAddress())

        if (supportedServerServices.isNotEmpty()) {
            val bluetoothService = App.context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?
                ?: return false.also { println("Error getting bluetoothManager") }

            bluetoothGattServer = bluetoothService.openGattServer(context, internalGattServerCallback)
                ?: return false.also { println("Error opening Gatt Server") }

            for (service in supportedServerServices) bluetoothGattServer!!.addService(service)
        }

        synchronized(gattMonitor) {
            bluetoothGatt = remoteWearable.connectGatt(context, false, internalGattCallback, BluetoothDevice.TRANSPORT_LE)
        }

        val result = bluetoothGatt != null

        if (result) setWearableConnectionState(Wearable.State.CONNECTING)

        return result
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    fun disconnect() {
        synchronized(gattMonitor) {
            println("disconnect()")

            val gatt = bluetoothGatt

            if (gatt != null) {
                bluetoothGatt = null
                println("Disconnecting BtLEQueue from GATT device")

                gatt.disconnect()
                gatt.close()
                setWearableConnectionState(Wearable.State.NOT_CONNECTED)
            }

            pauseTransaction = false
            val gattServer = bluetoothGattServer

            if (gattServer != null) {
                bluetoothGattServer = null
                gattServer.clearServices()
                gattServer.close()
            }
        }
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    fun dispose() {
        if (disposed) return

        disposed = true
        disconnect()

        dispatchThread?.interrupt()
        dispatchThread = null
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    private fun handleDisconnected(status: Int) {
        println("handleDisconnected: $status")

        internalGattCallback.reset()
        transactions.clear()

        pauseTransaction = false
        abortTransaction = true
        abortServerTransaction = true

        waitForActionResultLatch?.countDown()
        waitForServerActionResultLatch?.countDown()

        setWearableConnectionState(Wearable.State.NOT_CONNECTED)

        if (bluetoothGatt != null && !maybeReconnect()) disconnect()
    }

    fun insert(transaction: Transaction) {
        println("about to insert: $transaction")

        if (transaction.isEmpty()) return

        val tail = ArrayList<AbstractTransaction>(transactions.size + 2)
        tail.addAll(transactions)
        transactions.clear()
        transactions.add(transaction)
        transactions.addAll(tail)
    }

    private fun isConnected() = when (wearable.isConnected()) {
        true -> true
        false -> false.also { println("isConnected(): current state = ${wearable.getState()}") }
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    private fun maybeReconnect(): Boolean {
        if (autoReconnect && bluetoothGatt != null) {
            if (scanReconnect) {
                println("Waiting for BLE scan before attempting reconnection...")
                setWearableConnectionState(Wearable.State.WAITING_FOR_SCAN)
                return true
            }

            println("Enabling automatic ble reconnect...")

            val result = bluetoothGatt!!.connect()
            pauseTransaction = false

            if (result) setWearableConnectionState(Wearable.State.WAITING_FOR_RECONNECT)

            return result
        }

        return false
    }

    fun setAutoReconnect(autoReconnect: Boolean) {
        this.autoReconnect = autoReconnect
    }

    fun setImplicitGattCallbackModify(implicitGattCallbackModify: Boolean) {
        this.implicitGattCallbackModify = implicitGattCallbackModify
    }

    fun setScanReconnect(scanReconnect: Boolean) {
        this.scanReconnect = scanReconnect
    }

    fun setSendWriteRequestResponse(sendWriteRequestResponse: Boolean) {
        this.sendWriteRequestResponse = sendWriteRequestResponse
    }

    private fun setWearableConnectionState(newState: Wearable.State) {
        Handler(Looper.getMainLooper()).post {
            println("new device connection state: $newState")
            wearable.setState(newState)
            wearable.sendDeviceUpdateIntent(context, WearableUpdateSubjectEnum.CONNECTION_STATE)
        }
    }

    private inner class InternalGattCallback(private val externalGattCallback: BluetoothGattCallbackHandler): BluetoothGattCallback() {
        private var transactionGattCallback: BluetoothGattCallbackHandler? = null

        private fun checkWaitingCharacteristic(characteristic: BluetoothGattCharacteristic, status: Int) {
            if (status != BluetoothGatt.GATT_SUCCESS) {
                println("failed btle action, aborting transaction: ${characteristic.uuid} ${getStatusString(status)}")
                abortTransaction = true
            }

            if (characteristic.uuid.equals(waitCharacteristic?.uuid)) {
                waitForActionResultLatch?.countDown()
            } else if (waitCharacteristic != null) {
                println("checkWaitingCharacteristic: mismatched characteristic received: ${characteristic.uuid}")
            }
        }

        private fun getCallbackToUse() = transactionGattCallback ?: externalGattCallback

        private fun getStatusString(status: Int) = if (status == BluetoothGatt.GATT_SUCCESS) " (success)" else " (failed: $status)"

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            if (!checkCorrectGattInstance(gatt, "characteristic changed")) return

            try {
                getCallbackToUse().onCharacteristicChanged(gatt, characteristic)
            } catch (e: Throwable) {
                println("onCharacteristicChanged: ${e.message}")
            }
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            println("characteristic read: ${characteristic.uuid} ${getStatusString(status)}")

            if (!checkCorrectGattInstance(gatt, "characteristic read")) return

            try {
                getCallbackToUse().onCharacteristicRead(gatt, characteristic, status)
            } catch (e: Throwable) {
                println("onCharacteristicRead: ${e.message}")
            }

            checkWaitingCharacteristic(characteristic, status)
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            println("characteristic write: ${characteristic.uuid} ${getStatusString(status)}")

            if (!checkCorrectGattInstance(gatt, "characteristic write")) return

            getCallbackToUse().onCharacteristicWrite(gatt, characteristic, status)
            checkWaitingCharacteristic(characteristic, status)
        }

        @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            println("connection state change, newState: $newState ${getStatusString(status)}")

            synchronized(gattMonitor) {
                if (bluetoothGatt == null) bluetoothGatt = gatt
            }

            if (!checkCorrectGattInstance(gatt, "connection state event")) return

            if (status != BluetoothGatt.GATT_SUCCESS) println("connection state event with error status $status")

            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    println("Connected to GATT server.")
                    setWearableConnectionState(Wearable.State.CONNECTED)

                    val cachedServices = gatt?.services

                    if (!cachedServices.isNullOrEmpty()) {
                        println("Using cached services, skipping discovery")
                        onServicesDiscovered(gatt, BluetoothGatt.GATT_SUCCESS)
                    } else {
                        println("Attempting to start service discovery")

                        Handler(Looper.getMainLooper()).post {
                            bluetoothGatt?.discoverServices()
                        }
                    }
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    println("Disconnected from GATT server.")
                    handleDisconnected(status)
                }

                BluetoothProfile.STATE_CONNECTING -> {
                    println("Connecting to GATT server...")
                    setWearableConnectionState(Wearable.State.CONNECTING)
                }
            }
        }

        override fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
            println("descriptor read: ${descriptor.uuid} ${getStatusString(status)}")

            if (!checkCorrectGattInstance(gatt, "descriptor read")) return

            try {
                getCallbackToUse().onDescriptorRead(gatt, descriptor, status)
            } catch (e: Throwable) {
                println("onDescriptorRead: ${e.message}")
            }

            checkWaitingCharacteristic(descriptor.characteristic, status)
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
            println("descriptor write: ${descriptor.uuid} ${getStatusString(status)}")

            if (!checkCorrectGattInstance(gatt, "descriptor write")) return

            try {
                getCallbackToUse().onDescriptorWrite(gatt, descriptor, status)
            } catch (e: Throwable) {
                println("onDescriptorWrite: ${e.message}")
            }

            checkWaitingCharacteristic(descriptor.characteristic, status)
        }

        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            super.onMtuChanged(gatt, mtu, status)

            getCallbackToUse().onMtuChanged(gatt, mtu, status)
            waitForActionResultLatch?.countDown()
        }

        override fun onReadRemoteRssi(gatt: BluetoothGatt, rssi: Int, status: Int) {
            println("remote rssi: $rssi ${getStatusString(status)}")

            if (!checkCorrectGattInstance(gatt, "remote rssi")) return

            try {
                getCallbackToUse().onReadRemoteRssi(gatt, rssi, status)
            } catch (e: Throwable) {
                println("onReadRemoteRssi: ${e.message}")
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (!checkCorrectGattInstance(gatt, "services discovered: ${getStatusString(status)}")) return

            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    getCallbackToUse().onServicesDiscovered(gatt)
                    connectionLatch?.countDown()
                }

                else -> println("onServicesDiscovered received: $status")
            }
        }

        fun reset() {
            transactionGattCallback = null
        }

        fun setTransactionGattCallback(transactionGattCallback: BluetoothGattCallbackHandler?) {
            this.transactionGattCallback = transactionGattCallback
        }
    }

    private inner class InternalGattServerCallback(private val externalGattServerCallbackHandler: BluetoothGattServerCallbackHandler): BluetoothGattServerCallback() {
        private var transactionGattServerCallback: BluetoothGattServerCallbackHandler? = null

        private fun getCallbackToUse() = transactionGattServerCallback ?: externalGattServerCallbackHandler

        private fun getStatusString(status: Int) = if (status == BluetoothGatt.GATT_SUCCESS) " (success)" else " (failed: $status)"

        override fun onCharacteristicReadRequest(device: BluetoothDevice, requestId: Int, offset: Int, characteristic: BluetoothGattCharacteristic) {
            if (!checkCorrectBluetoothDevice(device)) return

            println("characteristic read request: ${device.address} characteristic: ${characteristic.uuid}")
            getCallbackToUse().onCharacteristicReadRequest(device, requestId, offset, characteristic)
        }

        @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
        override fun onCharacteristicWriteRequest(
            device: BluetoothDevice,
            requestId: Int,
            characteristic: BluetoothGattCharacteristic,
            preparedWrite: Boolean,
            responseNeeded: Boolean,
            offset: Int,
            value: ByteArray
        ) {
            if (!checkCorrectBluetoothDevice(device)) return

            println("characteristic write request: ${device.address} characteristic: ${characteristic.uuid}")
            val success = getCallbackToUse().onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value)

            if (responseNeeded && sendWriteRequestResponse) {
                bluetoothGattServer?.sendResponse(
                    device,
                    requestId,
                    if (success) BluetoothGatt.GATT_SUCCESS else BluetoothGatt.GATT_FAILURE,
                    0,
                    byteArrayOf()
                )
            }
        }

        override fun onConnectionStateChange(device: BluetoothDevice, status: Int, newState: Int) {
            println("gatt server connection state change, newState: $newState ${getStatusString(status)}")

            if (!checkCorrectBluetoothDevice(device)) return

            if (status != BluetoothGatt.GATT_SUCCESS) println("connection state event with error status $status")
        }

        override fun onDescriptorReadRequest(
            device: BluetoothDevice,
            requestId: Int,
            offset: Int,
            descriptor: BluetoothGattDescriptor
        ) {
            if (!checkCorrectBluetoothDevice(device)) return

            println("onDescriptorReadRequest: ${device.address}")
            getCallbackToUse().onDescriptorReadRequest(device, requestId, offset, descriptor)
        }

        @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
        override fun onDescriptorWriteRequest(
            device: BluetoothDevice,
            requestId: Int,
            descriptor: BluetoothGattDescriptor,
            preparedWrite: Boolean,
            responseNeeded: Boolean,
            offset: Int,
            value: ByteArray
        ) {
            if (!checkCorrectBluetoothDevice(device)) return

            println("onDescriptorWriteRequest: ${device.address}")
            val success = getCallbackToUse().onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value)

            if (responseNeeded && sendWriteRequestResponse) {
                bluetoothGattServer?.sendResponse(
                    device,
                    requestId,
                    if (success) BluetoothGatt.GATT_SUCCESS else BluetoothGatt.GATT_FAILURE,
                    0,
                    byteArrayOf()
                )
            }
        }

        fun setTransactionGattServerCallback(transactionGattServerCallback: BluetoothGattServerCallbackHandler?) {
            this.transactionGattServerCallback = transactionGattServerCallback
        }
    }
}