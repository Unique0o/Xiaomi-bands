package com.example.logifitappp.core.wearebles.huami.zeppos

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.icu.util.TimeUnit
import androidx.annotation.RequiresPermission
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.specs.CallSpec
import com.example.logifitappp.core.specs.NotificationSpec
import com.example.logifitappp.core.utils.BleTypeConversionsUtils
import com.example.logifitappp.core.utils.GattCharacteristic
import com.example.logifitappp.core.wearebles.huami.Huami2021Service
import com.example.logifitappp.core.wearebles.huami.HuamiBatteryInfo
import com.example.logifitappp.core.wearebles.huami.HuamiService
import com.example.logifitappp.core.wearebles.huami.HuamiSupport
import com.example.logifitappp.core.wearebles.huami.zeppos.services.AbstractZeppOsService
import com.example.logifitappp.core.wearebles.huami.zeppos.services.ZeppOsNotificationService
import com.example.logifitappp.core.wearebles.huami.zeppos.services.ZeppOsServicesService
import org.apache.commons.lang3.ArrayUtils

class ZeppOsSupport: HuamiSupport() {
    private val notificationService = ZeppOsNotificationService(this)
    private val servicesService = ZeppOsServicesService(this)

    private val supportedServices = HashSet<Short>()
    private val isEncryptedSet = HashSet<Short>()

    private val serviceMap = linkedMapOf<Short, AbstractZeppOsService>(
        servicesService.getEndpoint() to servicesService,
        notificationService.getEndpoint() to notificationService
    )

    fun addSupportedService(endpoint: Short, encrypted: Boolean) {
        supportedServices.add(endpoint)

        if (encrypted) isEncryptedSet.add(endpoint)
    }

    override fun enableFurtherNotification(builder: TransactionBuilder, enable: Boolean): ZeppOsSupport {
        return this
    }

    override fun enableNotifications(builder: TransactionBuilder, enable: Boolean): HuamiSupport {
        builder.notify(getCharacteristic(HuamiService.UUID_CHARACTERISTIC_CHUNKEDTRANSFER_2021_READ), enable)
        return this
    }

    override fun force2021Protocol() = true

    override fun getAuthFlags() = 0x00.toByte()

    override fun getCoordinator() = getWearable().getWearableCoordinator() as ZeppOsCoordinator

    override fun getCryptFlags() = 0x80.toByte()

    override fun getImplicitCallbackModify() = false

    override fun getRawActivitySize() = 8

    fun getService(endpoint: Short) = serviceMap[endpoint]

    override fun getTimeBytes(calendar: Calendar, precision: TimeUnit): ByteArray {
        val bytes = BleTypeConversionsUtils.shortCalendarToRawBytes(calendar)

        return when (precision) {
            TimeUnit.MINUTE,
            TimeUnit.SECOND -> {
                val seconds = if (precision == TimeUnit.SECOND) BleTypeConversionsUtils.fromUint8(calendar.get(Calendar.SECOND)) else 0.toByte()
                val tz = BleTypeConversionsUtils.mapTimeZone(calendar, BleTypeConversionsUtils.TZ_FLAG_INCLUDE_DST_IN_TZ)

                BleTypeConversionsUtils.join(bytes, byteArrayOf(seconds, tz))!!
            }
            else -> throw IllegalArgumentException("Unsupported precision, only MINUTES and SECONDS are supported")
        }
    }

    private fun handle2021Battery(payload: ByteArray) {
        if (payload[0] != Huami2021Service.BATTERY_REPLY) {
            println("Unexpected battery payload byte ${payload[0]}")
            return
        }

        if (payload.size != 21) println("Unexpected battery payload length: ${payload.size}")

        val batteryInfo = HuamiBatteryInfo(ArrayUtils.subarray(payload, 1, payload.size))
        handleBatteryInfoEvent(batteryInfo.toWearableEvent())
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    private fun handle2021Connection(payload: ByteArray) {
        when (payload[0]) {
            Huami2021Service.CONNECTION_CMD_MTU_RESPONSE -> {
                val mtu = BleTypeConversionsUtils.toUint16(payload, 1) + 3
                println("Device announced MTU change: $mtu")
                setMtu(mtu)
            }

            Huami2021Service.CONNECTION_CMD_UNKNOWN_3 -> {
                println("Got unknown 3, replying with unknown 4")
                writeToChunked2021("respond connection unknown 4", Huami2021Service.CHUNKED2021_ENDPOINT_CONNECTION, Huami2021Service.CONNECTION_CMD_UNKNOWN_4, false)
            }

            else -> println("Unexpected connection payload byte ${payload[0]}")
        }
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    override fun handle2021Payload(type: Short, payload: ByteArray) {
        if (payload.isEmpty()) {
            println("Empty or null payload for $type")
            return
        }

        serviceMap[type]?.let {
            it.handlePayload(payload)
            return
        }

        when (type) {
            Huami2021Service.CHUNKED2021_ENDPOINT_AUTH -> println("Unexpected auth payload ${payload.contentToString()}")
            Huami2021Service.CHUNKED2021_ENDPOINT_COMPAT -> println("Unexpected compat payload ${payload.contentToString()}")
            Huami2021Service.CHUNKED2021_ENDPOINT_CONNECTION -> handle2021Connection(payload)
            Huami2021Service.CHUNKED2021_ENDPOINT_BATTERY -> handle2021Battery(payload)

            else -> println("Unhandled 2021 payload $type")
        }
    }

    fun initializeServices() {
        println("2021 initializeServices...")

        try {
            val builder = createTransactionBuilder("initialize services")

            serviceMap.values.forEach {
                if (supportedServices.contains(it.getEndpoint())) it.initialize(builder)
            }

            getQueue()?.let { builder.queue(it) }
        } catch (e: Exception) {
            println("failed initializing device $e")
        }
    }

    override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic): Boolean {
        if (characteristic.uuid == HuamiService.UUID_CHARACTERISTIC_ZEPP_OS_FILE_TRANSFER_V3) return true

        return super.onCharacteristicChanged(gatt, characteristic)
    }

    override fun onNotification(notificationSpec: NotificationSpec) {
        notificationService.sendNotification(notificationSpec)
    }

    override fun onSetCallState(callSpec: CallSpec) {
        notificationService.setCallState(callSpec)
    }

    override fun phase2Initialize(builder: TransactionBuilder) {
        println("2021 phase2Initialize...")
        requestMTU(builder)
        requestBatteryInfo(builder)
    }

    override fun phase3Initialize(builder: TransactionBuilder) {
        println("2021 phase3Initialize...")

        supportedServices.clear()
        isEncryptedSet.clear()
        servicesService.requestServices(builder)
    }

    override fun requestBatteryInfo(builder: TransactionBuilder): ZeppOsSupport {
        println("Requesting Battery Info")
        writeToChunked2021(builder, Huami2021Service.CHUNKED2021_ENDPOINT_BATTERY, Huami2021Service.BATTERY_REQUEST, false)

        return this
    }

    private fun requestMTU(builder: TransactionBuilder) {
        writeToChunked2021(builder, Huami2021Service.CHUNKED2021_ENDPOINT_CONNECTION, Huami2021Service.CONNECTION_CMD_MTU_REQUEST, false)
    }

    override fun requestGPSVersion(builder: TransactionBuilder): ZeppOsSupport {
        println("Request GPS version not implemented")
        return this
    }

    override fun setCurrentTimeWithService(builder: TransactionBuilder): ZeppOsSupport {
        val timestamp = GregorianCalendar()
        val year = BleTypeConversionsUtils.fromUint16(timestamp.get(Calendar.YEAR))

        val cmd = byteArrayOf(
            year[0],
            year[1],
            BleTypeConversionsUtils.fromUint8(timestamp.get(Calendar.MONTH) + 1),
            BleTypeConversionsUtils.fromUint8(timestamp.get(Calendar.DATE)),
            BleTypeConversionsUtils.fromUint8(timestamp.get(Calendar.HOUR_OF_DAY)),
            BleTypeConversionsUtils.fromUint8(timestamp.get(Calendar.MINUTE)),
            BleTypeConversionsUtils.fromUint8(timestamp.get(Calendar.SECOND)),
            BleTypeConversionsUtils.fromUint8(timestamp.get(Calendar.DAY_OF_WEEK)),
            0x00,
            0x08,
            BleTypeConversionsUtils.mapTimeZone(timestamp, BleTypeConversionsUtils.TZ_FLAG_INCLUDE_DST_IN_TZ)
        )

        builder.write(getCharacteristic(GattCharacteristic.UUID_CHARACTERISTIC_CURRENT_TIME), cmd)
        return this
    }

    override fun writeToChunked2021(builder: TransactionBuilder, type: Short, payload: ByteArray, encrypt: Boolean) {
        huami2021ChunkedEncoder?.write(builder, type, payload, force2021Protocol(), isEncryptedSet.contains(type))
    }
}