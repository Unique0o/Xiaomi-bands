package com.example.logifitappp.core.wearebles.xiaomi

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.example.logifitappp.core.App
import com.example.logifitappp.core.CallSpec
import com.example.logifitappp.core.bluetooth.ConnectionTypeEnum
import com.example.logifitappp.core.wearebles.AbstractWearableSupport
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.xiaomi.services.XiaomiAuthService
import com.example.logifitappp.core.wearebles.xiaomi.services.XiaomiHealthService
import com.example.logifitappp.core.wearebles.xiaomi.services.XiaomiSystemService
import nodomain.freeyourgadget.gadgetbridge.proto.xiaomi.XiaomiProto

class XiaomiSupport: AbstractWearableSupport() {
    private val authService = XiaomiAuthService(this)
    private var cachedFirmwareVersion: String? = null
    private var connectionSupport: XiaomiConnectionSupport? = null
    private val healthService = XiaomiHealthService(this)
    private val systemService = XiaomiSystemService(this)

    private val serviceMap = linkedMapOf(
        XiaomiAuthService.COMMAND_TYPE to authService,
        XiaomiHealthService.COMMAND_TYPE to healthService,
        XiaomiSystemService.COMMAND_TYPE to systemService
    )

    override fun connect(): Boolean {
        return getConnectionSpecificSupport()?.connect() ?: false.also {
            println("getConnectionSpecificSupport returned null, could not connect")
        }
    }

    private fun createConnectionSpecificSupport(): XiaomiConnectionSupport? {
        return when (getCoordinator().getConnectionType()) {
            ConnectionTypeEnum.BOTH,
            ConnectionTypeEnum.BLE -> XiaomiBleConnectionSupport(this)

            ConnectionTypeEnum.BT_CLASSIC -> {
                // TODO WHEN IS REQUIRED
                null
            }
        }
    }

    override fun dispose() {
        val connectionSupport = this.connectionSupport
        this.connectionSupport = null

        connectionSupport?.dispose()
    }

    fun getAuthService() = authService

    fun getCachedFirmwareVersion(): String? = cachedFirmwareVersion

    private fun getConnectionSpecificSupport(): XiaomiConnectionSupport? {
        if (connectionSupport == null) connectionSupport = createConnectionSpecificSupport()

        return connectionSupport
    }

    private fun getCoordinator(): XiaomiCoordinator {
        return getWearable().getWearableCoordinator() as XiaomiCoordinator
    }

    fun getHealthService() = healthService

    fun handleCommandBytes(payload: ByteArray) {
        println("Got command: ${payload.contentToString()}")

        val cmd = try {
            XiaomiProto.Command.parseFrom(payload)
        } catch (e: Exception) {
            println("Failed to parse bytes as protobuf command payload")
            return
        }

        val service = serviceMap[cmd.type]
        if (service != null) {
            service.handleCommand(cmd)
            return
        }

        println("Unexpected watch command type ${cmd.type}")
    }

    fun onAuthSuccess() {
        println("onAuthSuccess")

        getConnectionSpecificSupport()?.onAuthSuccess()

        if (App.preferences.getBoolean("datetime_synconconnect", true)) systemService.setCurrentTime()

        serviceMap.values.forEach { it.initialize() }
    }

    fun onDisconnect() {
        serviceMap.values.forEach { it.onDisconnect() }
    }

    override fun onFetchRecordedData(dataTypes: Int) {
        healthService.onFetchRecordedData(dataTypes)
    }

    override fun onSetCallState(callSpec: CallSpec) {

    }

    fun sendCommand(taskName: String, command: XiaomiProto.Command) {
        getConnectionSpecificSupport()?.sendCommand(taskName, command)
    }

    fun sendCommand(taskName: String, type: Int, subtype: Int) {
        sendCommand(
            taskName,
            XiaomiProto.Command.newBuilder()
                .setType(type)
                .setSubtype(subtype)
                .build()
        )
    }

    override fun setAutoReconnect(enabled: Boolean) {
        super.setAutoReconnect(enabled)

        connectionSupport?.setAutoReconnect(enabled)
    }

    override fun setContext(wearable: Wearable, adapter: BluetoothAdapter?, context: Context) {
        if (wearable.getFirmwareVersion() != null) {
            cachedFirmwareVersion = wearable.getFirmwareVersion()
        }

        super.setContext(wearable, adapter, context)

        serviceMap.values.forEach { it.setContext(context) }

        getConnectionSpecificSupport()?.setContext(wearable, adapter!!, context)
    }

    override fun useAutoConnect() = true
}