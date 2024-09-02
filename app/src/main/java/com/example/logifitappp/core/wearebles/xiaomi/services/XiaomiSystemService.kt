package com.example.logifitappp.core.wearebles.xiaomi.services

import android.os.Handler
import android.os.Looper
import com.example.logifitappp.core.App
import com.example.logifitappp.core.AppPreferences
import com.example.logifitappp.core.events.WearableBatteryInfoEvent
import com.example.logifitappp.core.events.WearableUpdatePreferencesEvent
import com.example.logifitappp.core.events.WearableVersionInfoEvent
import com.example.logifitappp.core.wearebles.WearableSettingPreferenceConstants
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiPreferences
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiSupport
import nodomain.freeyourgadget.gadgetbridge.proto.xiaomi.XiaomiProto
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone

class XiaomiSystemService(support: XiaomiSupport) : AbstractXiaomiService(support) {
    private val handler = Handler(Looper.getMainLooper())

    private val batteryStateRequestRunnable = Runnable {
        support.apply {
            sendCommand("get device status", COMMAND_TYPE, CMD_DEVICE_STATE_GET)
            sendCommand("get battery state", COMMAND_TYPE, CMD_BATTERY)
        }
    }

    fun setCurrentTime() {
        println("Setting current time")

        val now = GregorianCalendar.getInstance()
        val tz = TimeZone.getDefault()

        val preferences = AppPreferences(App.getWearableSpecificSharedPrefs(support.getWearable().getAddress())!!)
        val timeFormat = preferences.getTimeFormat()
        val is24Hours = WearableSettingPreferenceConstants.PREF_TIME_FORMAT_24H == timeFormat

        val clock = XiaomiProto.Clock.newBuilder()
            .setTime(
                XiaomiProto.Time.newBuilder()
                    .setHour(now.get(Calendar.HOUR_OF_DAY))
                    .setMinute(now.get(Calendar.MINUTE))
                    .setSecond(now.get(Calendar.SECOND))
                    .setMillisecond(now.get(Calendar.MILLISECOND))
                    .build()
            )
            .setDate(
                XiaomiProto.Date.newBuilder()
                    .setYear(now.get(Calendar.YEAR))
                    .setMonth(now.get(Calendar.MONTH) + 1)
                    .setDay(now.get(Calendar.DATE))
                    .build()
            )
            .setTimezone(
                XiaomiProto.TimeZone.newBuilder()
                    .setZoneOffset(((now.get(Calendar.ZONE_OFFSET) / 1000) / 60) / 15)
                    .setDstOffset(((now.get(Calendar.DST_OFFSET) / 1000) / 60) / 15)
                    .setName(tz.id)
                    .build()
            )
            .setIsNot24Hour(!is24Hours)
            .build()

        support.sendCommand(
            "set time",
            XiaomiProto.Command.newBuilder()
                .setType(COMMAND_TYPE)
                .setSubtype(CMD_CLOCK)
                .setSystem(
                    XiaomiProto.System.newBuilder()
                        .setClock(clock)
                        .build()
                )
                .build()
        )
    }

    private fun handleBasicDeviceState(basicDeviceState: XiaomiProto.BasicDeviceState?) {
        println("Got basic device state: $basicDeviceState")

        if (basicDeviceState == null) {
            println("Got null for BasicDeviceState, requesting battery state and returning")
            support.sendCommand("request battery state", COMMAND_TYPE, CMD_BATTERY)

            return
        }

        support.evaluateWearableEvent(WearableUpdatePreferencesEvent(XiaomiPreferences.FEAT_DEVICE_ACTIONS, true))

        if (!basicDeviceState.hasBatteryLevel()) support.sendCommand("get battery state", COMMAND_TYPE, CMD_BATTERY)
        else {
            val wearableBatteryInfoEvent = WearableBatteryInfoEvent()
            wearableBatteryInfoEvent.index = 0
            wearableBatteryInfoEvent.level = basicDeviceState.batteryLevel
            support.evaluateWearableEvent(wearableBatteryInfoEvent)
        }

        rearmBatteryStateRequestTimer()
    }

    private fun handleBattery(battery: XiaomiProto.Battery) {
        println("Got battery: ${battery.level}")

        val wearableBatteryInfoEvent = WearableBatteryInfoEvent()
        wearableBatteryInfoEvent.index = 0
        wearableBatteryInfoEvent.level = battery.level
        support.evaluateWearableEvent(wearableBatteryInfoEvent)

        rearmBatteryStateRequestTimer()
    }

    override fun handleCommand(cmd: XiaomiProto.Command) {
        when (cmd.subtype) {
            CMD_DEVICE_INFO -> handleDeviceInfo(cmd.system.deviceInfo)
            CMD_BATTERY -> handleBattery(cmd.system.power.battery)
            CMD_DEVICE_STATE_GET -> handleBasicDeviceState(if (cmd.system.hasBasicDeviceState()) cmd.system.basicDeviceState else null)
        }
    }

    private fun handleDeviceInfo(deviceInfo: XiaomiProto.DeviceInfo) {
        println("Got device info: fw=${deviceInfo.firmware} hw=${deviceInfo.model} sn=${deviceInfo.serialNumber}")

        val wearableVersionInfoEvent = WearableVersionInfoEvent()
        wearableVersionInfoEvent.firmwareVersion = deviceInfo.firmware
        wearableVersionInfoEvent.model = deviceInfo.model
        support.evaluateWearableEvent(wearableVersionInfoEvent)
    }

    override fun initialize() {
        support.apply {
            sendCommand("get device info", COMMAND_TYPE, CMD_DEVICE_INFO)
            sendCommand("get device status", COMMAND_TYPE, CMD_DEVICE_STATE_GET)
            sendCommand("get battery state", COMMAND_TYPE, CMD_BATTERY)
        }

        rearmBatteryStateRequestTimer()
    }

    private fun rearmBatteryStateRequestTimer() {
        handler.removeCallbacks(batteryStateRequestRunnable)

        if (wearablePreferences.getBatteryPollingEnabled()) {
            handler.postDelayed(
                batteryStateRequestRunnable,
                wearablePreferences.getBatteryPollingIntervalMinutes() * 60 * 1000L
            )
        }
    }

    override fun onDisconnect() {
        handler.removeCallbacks(batteryStateRequestRunnable)
    }

    companion object {
        private const val CMD_BATTERY = 1
        private const val CMD_CLOCK = 3
        private const val CMD_DEVICE_INFO = 2
        private const val CMD_DEVICE_STATE_GET = 78
        const val COMMAND_TYPE = 2
    }
}