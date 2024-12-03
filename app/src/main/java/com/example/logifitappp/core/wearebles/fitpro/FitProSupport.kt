package com.example.logifitappp.core.wearebles.fitpro

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Intent
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Handler
import android.os.Looper
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.builders.ble.actions.SetWearableBusyAction
import com.example.logifitappp.core.builders.ble.actions.SetWearableStateAction
import com.example.logifitappp.core.builders.ble.profiles.WearableBatteryInfoProfile
import com.example.logifitappp.core.builders.ble.profiles.WearableInfoProfile
import com.example.logifitappp.core.builders.ble.profiles.parcelables.WearableBatteryInfo
import com.example.logifitappp.core.builders.ble.profiles.parcelables.WearableInfo
import com.example.logifitappp.core.events.WearableBatteryInfoEvent
import com.example.logifitappp.core.events.WearableVersionInfoEvent
import com.example.logifitappp.core.handlers.IntentListenerHandler
import com.example.logifitappp.core.utils.GattService
import com.example.logifitappp.core.utils.parcelableExtra
import com.example.logifitappp.core.wearebles.AbstractBleWearableSupport
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.FitProRawActivityModel
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import kotlin.math.abs
import kotlin.math.floor

class FitProSupport: AbstractBleWearableSupport() {
    private val batteryCmd = WearableBatteryInfoEvent()
    private val versionCmd = WearableVersionInfoEvent()

    private val batteryInfoProfile: WearableBatteryInfoProfile<FitProSupport>
    private val wearableInfoProfile: WearableInfoProfile<FitProSupport>

    private var readCharacteristic: BluetoothGattCharacteristic? = null
    private var writeCharacteristic: BluetoothGattCharacteristic? = null

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable {
        signalFetchingActivityDataFinish()
    }

    init {
        addSupportedService(GattService.UUID_SERVICE_DEVICE_INFORMATION)
        addSupportedService(GattService.UUID_SERVICE_BATTERY_SERVICE)

        val listener = object: IntentListenerHandler {
            override fun notify(intent: Intent) {
                when (intent.action) {
                    WearableInfoProfile.ACTION_DEVICE_INFO -> handleDeviceInfo(intent.parcelableExtra<WearableInfo>(WearableInfoProfile.EXTRA_DEVICE_INFO))
                    WearableBatteryInfoProfile.ACTION_BATTERY_INFO -> handleBatteryInfo(intent.parcelableExtra<WearableBatteryInfo>(WearableBatteryInfoProfile.EXTRA_BATTERY_INFO))
                }
            }
        }

        wearableInfoProfile = WearableInfoProfile(this)
        wearableInfoProfile.addListener(listener)
        addSupportedProfile(wearableInfoProfile)

        batteryInfoProfile = WearableBatteryInfoProfile(this)
        batteryInfoProfile.addListener(listener)
        addSupportedProfile(batteryInfoProfile)

        addSupportedService(FitProConstants.UUID_CHARACTERISTIC_RX)
        addSupportedService(FitProConstants.UUID_CHARACTERISTIC_UART)
    }

    private fun craftData(group: Byte, command: Byte): ByteArray {
        return craftData(group, command, byteArrayOf())
    }

    private fun craftData(group: Byte, command: Byte, value: Byte): ByteArray {
        return craftData(group, command, byteArrayOf(value))
    }

    private fun craftData(group: Byte, command: Byte, payload: ByteArray): ByteArray {
        val result = ByteArray(FitProConstants.DATA_TEMPLATE.size + payload.size)
        System.arraycopy(FitProConstants.DATA_TEMPLATE, 0, result, 0, FitProConstants.DATA_TEMPLATE.size)
        result[1] = (((FitProConstants.DATA_TEMPLATE.size + payload.size - 3) shr 8) and 0xff).toByte()
        result[2] = ((FitProConstants.DATA_TEMPLATE.size + payload.size - 3) and 0xff).toByte()
        result[3] = group
        result[5] = command
        result[6] = ((payload.size shr 8) and 0xff).toByte()
        result[7] = (payload.size and 0xff).toByte()
        System.arraycopy(payload, 0, result, 8, payload.size)

        return result
    }

    private fun decodeDateTime(payload: ByteArray): Calendar {
        val dateShort = ByteBuffer.wrap(payload).getShort().toInt()

        val day = dateShort and 0x1f
        val month = (dateShort shr 5) and 0xf
        val year = (dateShort shr 9) + 2000

        return GregorianCalendar.getInstance().apply {
            set(year, month - 1, day, 0, 0, 0)
        }
    }

    override fun getImplicitCallbackModify() = true

    private fun getSecondsOfDay(encodedTime: Int): Int {
        val hours = floor((encodedTime * 15) / 60f).toInt()
        val minutes = (encodedTime * 15) % 60

        return (hours * 3600) + (minutes * 60)
    }

    override fun getSendWriteRequestResponse() = false

    private fun getSleepSecondsOfDay(encodedTime: Int): Int {
        val hours = floor(encodedTime / 60f).toInt()
        val minutes = encodedTime % 60

        return (hours * 3600) + (minutes * 60)
    }

    fun handleBatteryInfo(info: WearableBatteryInfo?) {
        info?.let {
            println("FitPro battery info: $it")
            batteryCmd.level = info.percentCharged

            handleBatteryInfoEvent(batteryCmd)
        }
    }

    private fun handleDayTotalsData(payload: ByteArray) {
        println("FitPro handle day data length: ${payload.size}")

        if (payload.size < 10) return

        sendAck(payload[3], payload[1], payload[2], payload[5])
    }

    private fun handleDeviceInfo(payload: ByteArray) {
        println("FitPro device info2")

        if (payload.size < 20) return

        var start = 14
        var length = payload[start].toInt()

        start += length + 1
        length = payload[start].toInt()

        val bytesHw = ByteArray(length)
        System.arraycopy(payload, start + 1, bytesHw, 0, length)

        versionCmd.model = String(bytesHw, StandardCharsets.UTF_8)
        handleVersionInfoEvent(versionCmd)
    }

    fun handleDeviceInfo(info: WearableInfo?) {
        info?.let {
            println("FitPro device info: $it")
            versionCmd.model = "FitPro"
            versionCmd.firmwareVersion = it.firmwareRevision

            handleVersionInfoEvent(versionCmd)
        }
    }

    private fun handleHardwareDetails(payload: ByteArray) {
        println("FitPro hardware details")

        if (payload.size < 20) return

        TransactionBuilder("notification").apply {
            write(writeCharacteristic, craftData(FitProConstants.CMD_GROUP_BAND_INFO, FitProConstants.CMD_RX_BAND_INFO))
            getQueue()?.let { queue(it) }
        }
    }

    private fun handleSleepData(payload: ByteArray) {
        val dateArray = ByteArray(2)
        System.arraycopy(payload, 8, dateArray, 0, 2)

        val date = decodeDateTime(dateArray)
        val samples = mutableListOf<FitProRawActivityModel>()

        for (i in 12 ..< payload.size - 3 step 4) {
            val packet = ByteArray(4)
            System.arraycopy(payload, i, packet, 0, 4)

            val data = ByteBuffer.wrap(packet).getInt()
            val type = data and 0xff
            val encodedTime = data shr 16
            val seconds = getSleepSecondsOfDay(encodedTime)

            val now = date.clone() as Calendar
            now.add(Calendar.SECOND, seconds)
            val timestamp = now.timeInMillis / 1000L

            samples.add(FitProRawActivityModel(
                activeTimeMinutes = 15,
                heartRate =  -1,
                timestamp = timestamp,
                type = rawSleepTypeToUniqueType(type)
            ))
        }

        if (saveActivity(samples)) sendAck(payload[3], payload[1], payload[2], payload[5])
    }

    private fun handleStepData(payload: ByteArray) {
        val dateArray = ByteArray(2)
        System.arraycopy(payload, 8, dateArray, 0, 2)

        val date = decodeDateTime(dateArray)
        val samples = mutableListOf<FitProRawActivityModel>()

        for (i in 12 ..< payload.size - 7 step 8) {
            val packet = ByteArray(8)
            System.arraycopy(payload, i, packet, 0, 8)

            val data = ByteBuffer.wrap(packet).getLong()
            val steps = abs(data shr 52).toInt()
            val calories = (data and 0x7ffff).toInt()
            val type = ((data shr 19) and 0x1).toInt()
            val duration = ((data shr 48) and 0xf).toInt()
            val distance = ((data shr 32) and 0xffff).toInt()
            val encodedTime = ((data shr 21) and 0x7ff).toInt()
            val seconds = getSecondsOfDay(encodedTime)

            val now = date.clone() as Calendar
            now.add(Calendar.SECOND, seconds)
            val timestamp = now.timeInMillis / 1000L

            samples.add(FitProRawActivityModel(
                activeTimeMinutes = duration,
                caloriesBurnt = calories,
                distanceMeters = distance,
                heartRate =  -1,
                steps = steps,
                timestamp = timestamp,
                type = rawActivityTypeToUniqueType(type)
            ))
        }

        if (saveActivity(samples)) sendAck(payload[3], payload[1], payload[2], payload[5])
    }

    private fun indicateContinuedFetchingOperation() {
        handler.removeCallbacks(runnable)
    }

    private fun indicateFinishedFetchingOperation() {
        handler.postDelayed(runnable, 5000)
    }

    override fun initializeDevice(builder: TransactionBuilder): TransactionBuilder {
        builder.add(SetWearableStateAction(getWearable(), Wearable.State.INITIALIZING, getContext()))
        readCharacteristic = getCharacteristic(FitProConstants.UUID_CHARACTERISTIC_RX)
        writeCharacteristic = getCharacteristic(FitProConstants.UUID_CHARACTERISTIC_TX)

        builder.notify(readCharacteristic, true)
        builder.notify(getCharacteristic(GattService.UUID_SERVICE_BATTERY_SERVICE), true)
        builder.setCallback(this)

        wearableInfoProfile.requestDeviceInfo(builder)
        batteryInfoProfile.requestBatteryInfo(builder)
        batteryInfoProfile.enableNotify(builder, true)
        wearableInfoProfile.enableNotify(builder, true)

        builder.write(writeCharacteristic, craftData(FitProConstants.CMD_GROUP_GENERAL, FitProConstants.CMD_INIT1, 0x2))
        setTime(builder)
        builder.wait(2000)
        builder.write(writeCharacteristic, craftData(FitProConstants.CMD_GROUP_REQUEST_DATA, FitProConstants.CMD_INIT1))
        builder.wait(2000)
        builder.write(writeCharacteristic, craftData(FitProConstants.CMD_GROUP_REQUEST_DATA, FitProConstants.CMD_INIT2))
        builder.wait(2000)
        builder.write(writeCharacteristic, craftData(FitProConstants.CMD_GROUP_GENERAL, FitProConstants.CMD_INIT3, FitProConstants.VALUE_ON))
        builder.wait(2000)
        builder.write(writeCharacteristic, craftData(FitProConstants.CMD_GROUP_REQUEST_DATA, FitProConstants.VALUE_ON))
        builder.wait(2000)
        builder.write(writeCharacteristic, craftData(FitProConstants.CMD_GROUP_REQUEST_DATA, 0xf))
        builder.wait(2000)
        builder.write(writeCharacteristic, craftData(FitProConstants.CMD_GROUP_REQUEST_DATA, FitProConstants.CMD_GET_HW_INFO))
        builder.wait(2000)
        builder.write(writeCharacteristic, craftData(FitProConstants.CMD_GROUP_BAND_INFO, FitProConstants.CMD_RX_BAND_INFO))
        builder.wait(2000)
        builder.add(SetWearableStateAction(getWearable(), Wearable.State.INITIALIZED, getContext()))

        return builder
    }

    override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic): Boolean {
        super.onCharacteristicChanged(gatt, characteristic)
        println("Characteristic changed UUID: ${characteristic.uuid}")

        val payload = characteristic.value

        if (payload[0] != FitProConstants.DATA_HEADER) {
            indicateFinishedFetchingOperation()
            return false
        }

        if (payload.size <= 5) return false

        val command = payload[3]
        val param = payload[5]

        when (command) {
            FitProConstants.CMD_GROUP_RECEIVE_SPORTS_DATA -> {
                when (param) {
                    FitProConstants.RX_SPORTS_DAY_DATA -> {
                        indicateContinuedFetchingOperation()
                        handleDayTotalsData(payload)
                        indicateFinishedFetchingOperation()
                    }

                    FitProConstants.RX_SLEEP_DATA -> {
                        indicateContinuedFetchingOperation()
                        handleSleepData(payload)
                        indicateFinishedFetchingOperation()
                    }

                    FitProConstants.RX_STEP_DATA -> {
                        indicateContinuedFetchingOperation()
                        handleStepData(payload)
                        indicateFinishedFetchingOperation()
                    }

                    FitProConstants.CMD_REQUEST_STEPS_DATA0x7,
                    FitProConstants.CMD_REQUEST_STEPS_DATA0x8,
                    FitProConstants.CMD_REQUEST_STEPS_DATA0x10 -> sendAck(payload[3], payload[1], payload[2], payload[5])
                }
            }

            FitProConstants.CMD_GROUP_BAND_INFO -> {
                if (param == FitProConstants.CMD_RX_BAND_INFO) handleDeviceInfo(payload)

                sendAck(payload[3], payload[1], payload[2], payload[5])
            }

            FitProConstants.CMD_GROUP_REQUEST_DATA -> {
                if (param == FitProConstants.CMD_GET_HW_INFO) handleHardwareDetails(payload)

                sendAck(payload[3], payload[1], payload[2], payload[5])
            }
        }

        return false
    }

    override fun onFetchRecordedData(dataTypes: Int) {
        TransactionBuilder("fetch data1").apply {
            add(SetWearableBusyAction(getWearable(), getContext().getString(R.string.busy_task_fetch_activity_data), getContext()))
            write(writeCharacteristic, craftData(FitProConstants.CMD_GROUP_RECEIVE_SPORTS_DATA, FitProConstants.CMD_REQUEST_STEPS_DATA1, FitProConstants.VALUE_ON))
            getQueue()?.let { queue(it) }
        }
    }

    private fun rawActivityTypeToUniqueType(type: Int) = type

    private fun rawSleepTypeToUniqueType(type: Int) = type + 10

    private fun saveActivity(activities: List<FitProRawActivityModel>): Boolean {
        try {
            val provider = getWearable().getWearableCoordinator().getActivityProvider(getWearable()) as FitProActivityProvider
            provider.store(*activities.toTypedArray())
        } catch (e: Exception) {
            return false
        }

        return true
    }

    private fun sendAck(group: Byte, lengthHigh: Byte, lengthLow: Byte, command: Byte) {
        println("ACKing data: ${byteArrayOf(group).contentToString()} ${byteArrayOf(command).contentToString()}")

        val size = (ByteBuffer.wrap(byteArrayOf(lengthHigh, lengthLow)).getShort() + 3).toShort()
        val sizeArray = ByteBuffer.allocate(2).putShort(size).array()

        TransactionBuilder("notification").apply {
            write(writeCharacteristic, byteArrayOf(FitProConstants.DATA_HEADER_ACK, 0, 5, group, 1, sizeArray[0], sizeArray[1], 1))
            getQueue()?.let { queue(it) }
        }
    }

    fun setTime(builder: TransactionBuilder): FitProSupport {
        println("FitPro set time")
        val calendar = Calendar.getInstance()

        val datetime = calendar.get(Calendar.SECOND) or (
                    (calendar.get(Calendar.YEAR) - 2000) shl 26 or (calendar.get(Calendar.MONTH) + 1 shl 22)
                            or (calendar.get(Calendar.DAY_OF_MONTH) shl 17) or (calendar.get(Calendar.HOUR_OF_DAY) shl 12)
                            or (calendar.get(Calendar.MINUTE) shl 6))

        val buffer = ByteBuffer.allocate(4).putInt(datetime)
        val time = craftData(FitProConstants.CMD_GROUP_GENERAL, FitProConstants.CMD_SET_DATE_TIME, buffer.array())
        builder.write(writeCharacteristic, time)

        return this
    }

    fun signalFetchingActivityDataFinish() {
        App.signalFetchingActivityDataFinish(getWearable())
        unsetBusy()
    }

    private fun unsetBusy() {
        getWearable().apply {
            if (isBusy()) {
                unsetBusyTask()
                sendDeviceUpdateIntent(getContext())
            }
        }
    }

    override fun useAutoConnect() = true
}