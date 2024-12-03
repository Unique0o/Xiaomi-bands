package com.example.logifitappp.core.wearebles.huami

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Intent
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.icu.util.TimeUnit
import androidx.annotation.RequiresPermission
import com.example.logifitappp.core.App
import com.example.logifitappp.core.RecordedDataTypesEnum
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.builders.ble.actions.SetWearableStateAction
import com.example.logifitappp.core.events.WearableVersionInfoEvent
import com.example.logifitappp.core.handlers.IntentListenerHandler
import com.example.logifitappp.core.utils.BleTypeConversionsUtils
import com.example.logifitappp.core.utils.GattCharacteristic
import com.example.logifitappp.core.utils.GattService
import com.example.logifitappp.core.utils.parcelableExtra
import com.example.logifitappp.core.wearebles.AbstractBleWearableSupport
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.builders.ble.profiles.parcelables.WearableInfo
import com.example.logifitappp.core.builders.ble.profiles.WearableInfoProfile
import com.example.logifitappp.core.wearebles.huami.miband.MiBandService
import com.example.logifitappp.core.wearebles.huami.operations.AbstractFetchOperation
import com.example.logifitappp.core.wearebles.huami.operations.HuamiFetchActivityOperation
import com.example.logifitappp.core.wearebles.huami.operations.InitOperation
import com.example.logifitappp.core.wearebles.huami.operations.InitOperation2021
import okio.IOException
import org.apache.commons.lang3.ArrayUtils
import java.util.LinkedList

abstract class HuamiSupport: AbstractBleWearableSupport(), Huami2021Handler {
    private var rawActivitySize = 4
    private var characteristicChunked2021Read: BluetoothGattCharacteristic? = null
    private var characteristicChunked2021Write: BluetoothGattCharacteristic? = null
    private val fetchOperationQueue = LinkedList<AbstractFetchOperation>()
    private var huami2021ChunkedDecoder: Huami2021ChunkedDecoder? = null
    protected var huami2021ChunkedEncoder: Huami2021ChunkedEncoder? = null
    private var mtu = MIN_MTU
    private var needsAuth = false
    private var prevMtu = -1
    private var reassemblyBuffer: ByteArray? = null
    private var reassemblyType = 0x00.toByte()
    private val wearableInfoProfile: WearableInfoProfile<HuamiSupport>
    private val wearableVersionInfo = WearableVersionInfoEvent()

    private val listener = object: IntentListenerHandler {
        override fun notify(intent: Intent) {
            if (WearableInfoProfile.ACTION_DEVICE_INFO == intent.action) handleDeviceInfo(intent.parcelableExtra<WearableInfo>(
                WearableInfoProfile.EXTRA_DEVICE_INFO))
        }
    }

    init {
        addSupportedService(GattService.UUID_SERVICE_GENERIC_ACCESS)
        addSupportedService(GattService.UUID_SERVICE_GENERIC_ATTRIBUTE)
        addSupportedService(GattService.UUID_SERVICE_HEART_RATE)
        addSupportedService(GattService.UUID_SERVICE_IMMEDIATE_ALERT)
        addSupportedService(GattService.UUID_SERVICE_DEVICE_INFORMATION)
        addSupportedService(GattService.UUID_SERVICE_ALERT_NOTIFICATION)

        addSupportedService(MiBandService.UUID_SERVICE_MIBAND_SERVICE)
        addSupportedService(MiBandService.UUID_SERVICE_MIBAND2_SERVICE)
        addSupportedService(HuamiService.UUID_SERVICE_FIRMWARE_SERVICE)

        wearableInfoProfile = WearableInfoProfile(this)
        wearableInfoProfile.addListener(listener)
        addSupportedProfile(wearableInfoProfile)
    }

    protected fun allowHighMtu() = true

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    override fun connectFirstTime(): Boolean {
        needsAuth = true

        return super.connect()
    }

    private fun decodeAndUpdateAlarmStatus(response: ByteArray?, withTime: Boolean) {
        //TODO: alarms when required
    }

    open fun enableFurtherNotification(builder: TransactionBuilder, enable: Boolean): HuamiSupport {
        builder.notify(getCharacteristic(HuamiService.UUID_CHARACTERISTIC_3_CONFIGURATION), enable)
        builder.notify(getCharacteristic(HuamiService.UUID_CHARACTERISTIC_6_BATTERY_INFO), enable)
        characteristicChunked2021Read?.let { builder.notify(it, enable) }

        return this
    }

    open fun enableNotifications(builder: TransactionBuilder, enable: Boolean): HuamiSupport {
        builder.notify(getCharacteristic(HuamiService.UUID_CHARACTERISTIC_AUTH), enable)
        characteristicChunked2021Read?.let { builder.notify(it, enable) }

        return this
    }

    open fun force2021Protocol(): Boolean {
        return App.getWearableSpecificSharedPrefs(getWearable().getAddress())?.getBoolean("force_new_protocol", false) ?: false
    }

    fun fromTimeBytes(payload: ByteArray): Calendar {
        return BleTypeConversionsUtils.rawBytesToCalendar(payload)
    }

    protected open fun getAuthFlags() = HuamiService.AUTH_BYTE

    open fun getCoordinator() = getWearable().getWearableCoordinator() as HuamiCoordinator

    protected open fun getCryptFlags() = 0x00.toByte()

    fun getFetchOperationQueue() = fetchOperationQueue

    fun getFetchOperationsTimeUnit(): TimeUnit {
        val prefs = getWearablePrefs()
        val truncate = prefs.getBoolean("huami_truncate_fetch_operation_timestamps", true)

        return if (truncate) TimeUnit.MINUTE else TimeUnit.SECOND
    }

    override fun getImplicitCallbackModify() = true

    fun getNextFetchOperation(): AbstractFetchOperation? {
        return fetchOperationQueue.poll()
    }

    open fun getRawActivitySize() = rawActivitySize

    override fun getSendWriteRequestResponse() = false

    open fun getTimeBytes(calendar: Calendar, precision: TimeUnit): ByteArray {
        val bytes = when (precision) {
            TimeUnit.MINUTE -> BleTypeConversionsUtils.shortCalendarToRawBytes(calendar)
            TimeUnit.SECOND -> BleTypeConversionsUtils.calendarToRawBytes(calendar)
            else -> throw IllegalArgumentException("Unsupported precision, only MINUTES and SECONDS are supported till now")
        }

        val tail = byteArrayOf(0, BleTypeConversionsUtils.mapTimeZone(calendar, BleTypeConversionsUtils.TZ_FLAG_INCLUDE_DST_IN_TZ))

        return BleTypeConversionsUtils.join(bytes, tail)!!
    }

    protected fun handleDeviceInfo(info: WearableInfo?) {
        info?.let {
            wearableVersionInfo.model = it.hardwareRevision
            wearableVersionInfo.firmwareVersion = it.firmwareRevision ?: it.softwareRevision

            if (wearableVersionInfo.firmwareVersion != null && wearableVersionInfo.firmwareVersion!!.isNotEmpty() && wearableVersionInfo.firmwareVersion!![0] == 'V') {
                wearableVersionInfo.firmwareVersion = wearableVersionInfo.firmwareVersion!!.substring(1)
            }

            handleVersionInfoEvent(wearableVersionInfo)
        }
    }

    override fun handle2021Payload(type: Short, payload: ByteArray) {
        if (type == Huami2021Service.CHUNKED2021_ENDPOINT_COMPAT) {
            println("got configuration data")
            handleConfigurationInfo(ArrayUtils.remove(payload, 0))
            return
        }

        //unsafe for now in Gadgetbridge
    }

    private fun handleBatteryInfo(payload: ByteArray, status: Int) {
        if (status != BluetoothGatt.GATT_SUCCESS) return

        val info = HuamiBatteryInfo(payload)
        handleBatteryInfoEvent(info.toWearableEvent())
    }

    private fun handleChunked(payload: ByteArray) {
        when (payload[0]) {
            0x03.toByte() -> {
                huami2021ChunkedDecoder?.let {
                    if (it.decode(payload)) sendChunkedAck()
                } ?: println("Got chunked payload, but decoder is null")
            }

            0x04.toByte() -> println("Got chunked ack, handle=${payload[2]}, count=${payload[4]}")
            else -> println("Unhandled chunked payload of type ${payload[0]}")
        }
    }

    private fun handleConfigurationInfo(payload: ByteArray?) {
        if (payload == null || payload.size < 4) return

        if (payload[0] == 0x10.toByte() && payload[2] == 0x01.toByte()) {
            when (payload[1]) {
                HuamiService.COMMAND_GPS_VERSION -> {
                    val gpsVersion = String(payload, 3, payload.size - 3)
                    println("got gps version = $gpsVersion")
                }

                HuamiService.COMMAND_ALARMS -> {
                    println("got alarms from watch")
                    decodeAndUpdateAlarmStatus(payload, false)
                }

                else -> println("got configuration info we do not handle yet")
            }
        } else if (payload[0] == 0x80.toByte() && payload[1] == 0x01.toByte()) {
            var done = false

            if (payload[2] == 0x00.toByte() || payload[2] == 0xc0.toByte()) {
                reassemblyBuffer = ByteArray(payload.size - 8)
                reassemblyType = payload[4]

                reassemblyBuffer?.let { System.arraycopy(payload, 8, it, 0, it.size) }

                if (payload[2] == 0xc0.toByte()) done = true
            } else if (reassemblyBuffer != null && (payload[2] == 0x40.toByte() || payload[2] == 0x80.toByte())) {
                val buffer = ByteArray(payload.size - 4)
                System.arraycopy(payload, 4, buffer, 0, buffer.size)
                reassemblyBuffer = reassemblyBuffer!! + buffer

                if (payload[2] == 0x80.toByte()) done = true
            }

            if (!done) {
                println("got chunk of configuration data for ${reassemblyBuffer.contentToString()}")
                return
            }

            println("got full/reassembled configuration data")

            when (reassemblyType) {
                HuamiService.COMMAND_ALARMS_WITH_TIMES -> decodeAndUpdateAlarmStatus(reassemblyBuffer, true)
                HuamiService.COMMAND_WORKOUT_ACTIVITY_TYPES -> println("got workout activity types, not handled")
                else -> println("got unknown chunked configuration response for ${reassemblyBuffer.contentToString()}, not handled")
            }

            reassemblyBuffer = null
        } else println("unknown response got from configuration request")
    }

    override fun initializeDevice(builder: TransactionBuilder): TransactionBuilder {
        if (mtu != MIN_MTU) {
            prevMtu = mtu
            mtu = MIN_MTU
        }

        try {
            val authFlags = getAuthFlags()
            val cryptFlags = getCryptFlags()
            val authenticate = needsAuth && (cryptFlags == 0x00.toByte())

            needsAuth = false
            characteristicChunked2021Read = getCharacteristic(HuamiService.UUID_CHARACTERISTIC_CHUNKEDTRANSFER_2021_READ)

            if (characteristicChunked2021Read != null && huami2021ChunkedDecoder == null) {
                huami2021ChunkedDecoder = Huami2021ChunkedDecoder(this, force2021Protocol())
            }

            characteristicChunked2021Write = getCharacteristic(HuamiService.UUID_CHARACTERISTIC_CHUNKEDTRANSFER_2021_WRITE)

            if (characteristicChunked2021Write != null && huami2021ChunkedEncoder == null) {
                huami2021ChunkedEncoder = Huami2021ChunkedEncoder(characteristicChunked2021Write!!, force2021Protocol(), mtu)
            }

            if (force2021Protocol()) {
                if (characteristicChunked2021Write != null && characteristicChunked2021Read != null) {
                    InitOperation2021(this, authenticate, authFlags, cryptFlags, builder, huami2021ChunkedEncoder, huami2021ChunkedDecoder).perform()
                } else {
                    println("Chunked 2021 characteristics are null, will attempt to reconnect")
                    builder.add(SetWearableStateAction(getWearable(), Wearable.State.WAITING_FOR_RECONNECT, getContext()))
                }
            } else InitOperation(this, authenticate, authFlags, cryptFlags, builder).perform()
        } catch (e: IOException) {
            println("Initializing Huami device failed")
        }

        return builder
    }

    override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic): Boolean {
        if (super.onCharacteristicChanged(gatt, characteristic)) return true

        when (characteristic.uuid) {
            HuamiService.UUID_CHARACTERISTIC_6_BATTERY_INFO -> {
                handleBatteryInfo(characteristic.value, BluetoothGatt.GATT_SUCCESS)
                return true
            }

            HuamiService.UUID_CHARACTERISTIC_AUTH -> {
                println("AUTHENTICATION?? ${characteristic.uuid}")
                return true
            }

            HuamiService.UUID_CHARACTERISTIC_3_CONFIGURATION -> {
                handleConfigurationInfo(characteristic.value)
                return true
            }

            HuamiService.UUID_CHARACTERISTIC_CHUNKEDTRANSFER_2021_READ -> {
                handleChunked(characteristic.value)
                return true
            }

            else -> println("Unhandled characteristic changed: ${characteristic.uuid}")
        }

        return false
    }

    override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int): Boolean {
        if (super.onCharacteristicRead(gatt, characteristic, status)) return true

        when (characteristic.uuid) {
            HuamiService.UUID_CHARACTERISTIC_6_BATTERY_INFO -> {
                handleBatteryInfo(characteristic.value, status)
                return true
            }

            else -> println("Unhandled characteristic read: ${characteristic.uuid}")
        }

        return false
    }

    override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int): Boolean {
        if (characteristic.uuid == HuamiService.UUID_CHARACTERISTIC_AUTH) return true.also { println("KEY AES SEND") }

        return false
    }

    override fun onFetchRecordedData(dataTypes: Int) {
        if ((dataTypes and RecordedDataTypesEnum.TYPE_ACTIVITY) != 0) {
            println("operation: HuamiFetchActivityOperation")
            fetchOperationQueue.add(HuamiFetchActivityOperation(this))
        }

        fetchOperationQueue.poll()?.let {
            try {
                it.perform()
            } catch (e: IOException) {
                println("Unable to fetch recorded data: $e")
            }
        }
    }

    open fun phase2Initialize(builder: TransactionBuilder) {
        println("phase2Initialize...")

        if (prevMtu > MIN_MTU) {
            builder.requestMtu(prevMtu)
            prevMtu = -1
        }

        requestBatteryInfo(builder)
    }

    open fun phase3Initialize(builder: TransactionBuilder) {
        //TODO: do settings when are required
    }

    protected open fun requestBatteryInfo(builder: TransactionBuilder): HuamiSupport {
        println("Requesting Battery Info!")
        builder.read(getCharacteristic(HuamiService.UUID_CHARACTERISTIC_6_BATTERY_INFO))

        return this
    }

    fun requestDeviceInfo(builder: TransactionBuilder): HuamiSupport {
        println("Requesting Device Info!")
        wearableInfoProfile.requestDeviceInfo(builder)

        return this
    }

    open fun requestGPSVersion(builder: TransactionBuilder): HuamiSupport {
        println("Requesting GPS version")
        writeToConfiguration(builder, HuamiService.COMMAND_REQUEST_GPS_VERSION)

        return this
    }

    fun sendChunkedAck() {
        if (characteristicChunked2021Read ==  null) {
            println("Chunked read characteristic is null, can't send ack")
            return
        }

        val handle = huami2021ChunkedDecoder?.lastHandle ?: 0
        val count = huami2021ChunkedDecoder?.lastCount ?: 0

        try {
            val builder = createTransactionBuilder("send chunked ack")
            builder.write(characteristicChunked2021Read, byteArrayOf(0x04, 0x00, handle, 0x01, count))
            getQueue()?.let { builder.queue(it) }
        } catch (e: Exception) {
            println("Failed to send chunked ack")
        }
    }

    open fun setCurrentTimeWithService(builder: TransactionBuilder): HuamiSupport {
        val calendar = GregorianCalendar()
        val bytes = getTimeBytes(calendar, TimeUnit.SECOND)

        builder.write(getCharacteristic(GattCharacteristic.UUID_CHARACTERISTIC_CURRENT_TIME), bytes)
        return this
    }

    fun setInitialized(builder: TransactionBuilder) {
        builder.add(SetWearableStateAction(getWearable(), Wearable.State.INITIALIZED, getContext()))
    }

    protected fun setMtu(mtu: Int) {
        if (mtu > MIN_MTU && !allowHighMtu()) {
            println("High MTU is not allowed, ignoring")
            return
        }

        if (mtu < MIN_MTU) {
            println("Device announced unreasonable low MTU of $mtu, ignoring")
            return
        }

        this.mtu = mtu
        huami2021ChunkedEncoder?.setMtu(mtu)
    }

    override fun useAutoConnect() = true

    fun writeToChunked2021(builder: TransactionBuilder, type: Short, byte: Byte, encrypt: Boolean) {
        writeToChunked2021(builder, type, byteArrayOf(byte), encrypt)
    }

    open fun writeToChunked2021(builder: TransactionBuilder, type: Short, payload: ByteArray, encrypt: Boolean) {
        huami2021ChunkedEncoder?.write(builder, type, payload, force2021Protocol(), encrypt)
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    fun writeToChunked2021(taskName: String, type: Short, byte: Byte, encrypt: Boolean) {
        writeToChunked2021(taskName, type, byteArrayOf(byte), encrypt)
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    fun writeToChunked2021(taskName: String, type: Short, payload: ByteArray, encrypt: Boolean) {
        try {
            val builder = performInitialized(taskName)
            writeToChunked2021(builder, type, payload, encrypt)
            getQueue()?.let { builder.queue(it) }
        } catch (e: Exception) {
            println("Failed to $e")
        }
    }

    private fun writeToConfiguration(builder: TransactionBuilder, payload: ByteArray) {
        if (force2021Protocol()) {
            val data = ArrayUtils.insert(0, payload, 1)
            writeToChunked2021(builder, Huami2021Service.CHUNKED2021_ENDPOINT_COMPAT, data, true)
        } else builder.write(getCharacteristic(HuamiService.UUID_CHARACTERISTIC_3_CONFIGURATION), payload)
    }

    companion object {
        const val MIN_MTU = 23
    }
}