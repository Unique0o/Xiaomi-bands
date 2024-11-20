package com.example.logifitappp.core.wearebles.huami.operations

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import androidx.annotation.RequiresPermission
import com.example.logifitappp.core.App
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.builders.ble.actions.SetWearableBusyAction
import com.example.logifitappp.core.utils.BleTypeConversionsUtils
import com.example.logifitappp.core.utils.CheckSumUtils
import com.example.logifitappp.core.wearebles.huami.HuamiCoordinator
import com.example.logifitappp.core.wearebles.huami.HuamiService
import com.example.logifitappp.core.wearebles.huami.HuamiSupport
import com.example.logifitappp.core.wearebles.huami.zeppos.ZeppOsSupport
import okio.IOException
import org.bouncycastle.util.Arrays
import java.io.ByteArrayOutputStream

abstract class AbstractFetchOperation(support: HuamiSupport): AbstractHuamiOperation(support) {
    protected val buffer = ByteArrayOutputStream(1024)
    private var characteristicActivityData: BluetoothGattCharacteristic? = null
    private var characteristicFetch: BluetoothGattCharacteristic? = null
    private var expectedDataLength = 0
    protected var fetchCount = 0
    private var lastPacketCounter = 0.toByte()
    private var operationValid = true
    protected var startTimestamp: Calendar? = null

    private fun bufferActivityData(payload: ByteArray) {
        buffer.write(payload, 1, payload.size - 1)
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    override fun doPerform() {
        startFetching()
    }

    override fun enableNeededNotifications(builder: TransactionBuilder, enable: Boolean) {
        if (enable) return

        builder.notify(characteristicFetch, false)
        builder.notify(characteristicActivityData, false)
    }

    protected fun getLastSuccessfulSyncTime(): GregorianCalendar {
        val timestampMillis = App.getWearableSpecificSharedPrefs(wearable.getAddress())?.getLong(getLastSyncTimeKey(), 0) ?: 0

        if (timestampMillis != 0L) {
            return GregorianCalendar().apply {
                timeInMillis = timestampMillis
            }
        }

        return GregorianCalendar().apply {
            add(Calendar.DAY_OF_MONTH, -100)
        }
    }

    protected fun handleActivityData(payload: ByteArray) {
        println("${getName()} data: ${payload.contentToString()}")

        if (!isOperationRunning) {
            println("ignoring ${getName()} notification because operation is not running. Data length: ${payload.size}")
            return
        }

        if ((lastPacketCounter + 1).toByte() == payload[0]) {
            lastPacketCounter++
            bufferActivityData(payload)
        } else {
            println("Error ${getName()}, invalid package counter: ${payload[0]}, last was: $lastPacketCounter")
            operationValid = false
        }
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    private fun handleActivityMetadata(payload: ByteArray) {
        if (payload.size < 3) {
            println("Activity metadata too short: ${payload.contentToString()}")
            onOperationFinished()
            return
        }

        if (payload[0] != HuamiService.RESPONSE) {
            println("Activity metadata not a response: ${payload.contentToString()}")
            onOperationFinished()
            return
        }

        when (payload[1]) {
            HuamiService.COMMAND_ACTIVITY_DATA_START_DATE -> handleStartDateResponse(payload)
            HuamiService.COMMAND_FETCH_DATA -> handleFetchDataResponse(payload)

            HuamiService.COMMAND_ACK_ACTIVITY_DATA -> {
                println("Got reply to COMMAND_ACK_ACTIVITY_DATA")
                onOperationFinished()
            }

            else -> {
                println("Unexpected activity metadata: ${payload.contentToString()}")
                onOperationFinished()
            }
        }
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    private fun handleFetchDataResponse(payload: ByteArray) {
        if (payload[2] != HuamiService.SUCCESS) {
            println("Fetch data unsuccessful response: ${payload.contentToString()}")
            onOperationFinished()
            return
        }

        if (payload.size != 3 && payload.size != 7) {
            println("Fetch data unexpected metadata length: ${payload.contentToString()}")
            onOperationFinished()
            return
        }

        if (payload.size == 7 && !validChecksum(BleTypeConversionsUtils.toUint32(payload, 3))) {
            println("Data checksum invalid")

            if (isZeppOs()) {
                sendAck(true)
                return
            }

            onOperationFinished()
            return
        }

        val success = operationValid && processBufferedData()

        val keepActivityDataOnDevice = !success || HuamiCoordinator.getKeepActivityDataOnDevice(
            wearable.getAddress()!!
        )

        if (isZeppOs() || !keepActivityDataOnDevice) {
            sendAck(keepActivityDataOnDevice)
            return
        }

        onOperationFinished()
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    private fun handleStartDateResponse(payload: ByteArray) {
        if (payload[2] != HuamiService.SUCCESS) {
            println("Start date unsuccessful response: ${payload.contentToString()}")
            onOperationFinished()
            return
        }

        if (payload.size != 15 && (payload.size != 16 && payload[15] != 0x00.toByte())) {
            println("Start date response length: ${payload.contentToString()}")
            onOperationFinished()
            return
        }

        expectedDataLength = BleTypeConversionsUtils.toUint32(*Arrays.copyOfRange(payload, 3, 7))

        val startTimestamp = getSupport().fromTimeBytes(Arrays.copyOfRange(payload, 7, payload.size))

        if (expectedDataLength == 0) {
            println("No data to fetch since ${startTimestamp.time}")
            sendAck(true)
            return
        }

        this.startTimestamp = startTimestamp
        println("Will transfer $expectedDataLength packets since ${startTimestamp.time}")

        val step2builder = createTransactionBuilder("${getName()} Step 2")
        step2builder.notify(characteristicActivityData, true)
        step2builder.write(characteristicFetch, byteArrayOf(HuamiService.COMMAND_FETCH_DATA))

        try {
            performImmediately(step2builder)
        } catch (e: IOException) {
            println("Error starting fetch step 2: $e")
            onOperationFinished()
        }
    }

    private fun isZeppOs() = getSupport() is ZeppOsSupport

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic): Boolean {
        when (characteristic.uuid) {
            HuamiService.UUID_CHARACTERISTIC_5_ACTIVITY_DATA -> {
                handleActivityData(characteristic.value)
                return true
            }

            HuamiService.UUID_UNKNOWN_CHARACTERISTIC4 -> {
                handleActivityMetadata(characteristic.value)
                return true
            }

            else -> return super.onCharacteristicChanged(gatt, characteristic)
        }
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    private fun onOperationFinished() {
        getSupport().getNextFetchOperation()?.let {
            println("Performing next operation ${it.getName()}")

            try {
                it.perform()
                return
            } catch (e: IOException) {
                return
            }
        }

        println("All operations finished")

        App.signalActivityDataFinish(wearable)
        operationFinished()
        unsetBusy()
    }

    protected fun saveLastSyncTimestamp(timestamp: GregorianCalendar) {
        App.getWearableSpecificSharedPrefs(wearable.getAddress())?.edit()?.let {
            it.putLong(getLastSyncTimeKey(), timestamp.timeInMillis)
            it.apply()
        }
    }

    private fun sendAck(keepDataOnDevice: Boolean) {
        val ackBytes = when {
            isZeppOs() -> {
                println("Sending ack, keepDataOnDevice = $keepDataOnDevice")

                val ackByte = (if (keepDataOnDevice) 0x09 else 0x01).toByte()
                byteArrayOf(HuamiService.COMMAND_ACK_ACTIVITY_DATA, ackByte)
            }

            else -> {
                println("Sending ack, simple")
                byteArrayOf(HuamiService.COMMAND_ACK_ACTIVITY_DATA)
            }
        }

        try {
            val builder = createTransactionBuilder("${getName()} end")
            builder.write(characteristicFetch, ackBytes)
            performImmediately(builder)
        } catch (e: IOException) {
            println("Failed to send ack: $e")
        }
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    protected fun startFetching() {
        expectedDataLength = 0
        lastPacketCounter = -1

        val builder = performInitialized(getName())

        if (fetchCount == 0) builder.add(SetWearableBusyAction(wearable, taskDescription(), context))

        fetchCount++

        characteristicActivityData = getCharacteristic(HuamiService.UUID_CHARACTERISTIC_5_ACTIVITY_DATA)
        builder.notify(characteristicActivityData, false)

        characteristicFetch = getCharacteristic(HuamiService.UUID_UNKNOWN_CHARACTERISTIC4)
        builder.notify(characteristicFetch, true)

        startFetching(builder)
        queue?.let { builder.queue(it) }
    }

    protected fun startFetching(builder: TransactionBuilder, fetchType: Byte, sinceWhen: GregorianCalendar) {
        val support = getSupport()
        val fetchBytes = BleTypeConversionsUtils.join(
            byteArrayOf(HuamiService.COMMAND_ACTIVITY_DATA_START_DATE, fetchType),
            support.getTimeBytes(sinceWhen, support.getFetchOperationsTimeUnit())
        )

        builder.write(characteristicFetch, fetchBytes!!)
    }

    protected open fun validChecksum(crc32: Int) = crc32 == CheckSumUtils.getCRC32(buffer.toByteArray())

    abstract fun getLastSyncTimeKey(): String
    protected abstract fun processBufferedData(): Boolean
    abstract fun startFetching(builder: TransactionBuilder)
    abstract fun taskDescription(): String
}