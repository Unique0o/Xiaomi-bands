package com.example.logifitappp.core.wearebles.xiaomi.services

import com.example.logifitappp.core.wearebles.WearablePreferenceConst
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiActivityFileFetcher
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiActivityFileId
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiSupport
import com.google.protobuf.ByteString
import nodomain.freeyourgadget.gadgetbridge.proto.xiaomi.XiaomiProto
import java.nio.ByteBuffer
import java.nio.ByteOrder

class XiaomiHealthService(support: XiaomiSupport): AbstractXiaomiService(support) {
    private val activityFetcher = XiaomiActivityFileFetcher(this)

    fun ackRecordedData(fileId: XiaomiActivityFileId) {
        support.sendCommand(
            "ack recorded data",
            XiaomiProto.Command.newBuilder()
                .setType(COMMAND_TYPE)
                .setSubtype(CMD_ACTIVITY_FETCH_ACK)
                .setHealth(XiaomiProto.Health.newBuilder().setActivitySyncAckFileIds(
                    ByteString.copyFrom(fileId.toBytes())
                ))
                .build()
        )
    }

    override fun dispose() {
        activityFetcher.dispose()
    }

    override fun initialize() {
        setHeartRateConfig()
        //setSpo2Config()
        setStressConfig()
    }

    private fun fetchRecordedDataPast() {
        support.sendCommand(
            "fetch recorded data past",
            XiaomiProto.Command.newBuilder()
                .setType(COMMAND_TYPE)
                .setSubtype(CMD_ACTIVITY_FETCH_PAST)
                .build()
        )
    }

    private fun fetchRecordedDataToday() {
        support.sendCommand(
            "fetch recorded data today",
            XiaomiProto.Command.newBuilder()
                .setType(COMMAND_TYPE)
                .setSubtype(CMD_ACTIVITY_FETCH_TODAY)
                .setHealth(XiaomiProto.Health.newBuilder().setActivitySyncRequestToday(
                    XiaomiProto.ActivitySyncRequestToday.newBuilder().setUnknown1(0)
                ))
                .build()
        )
    }

    fun getActivityFetcher() = activityFetcher

    private fun handleActivityFetchResponse(subtype: Int, recordIds: ByteArray) {
        if (recordIds.size % 7 != 0) {
            println("recordIds ${recordIds.contentToString()} length = ${recordIds.size}, not a multiple of 7, can't parse")
            return
        }

        println("Got ${recordIds.size / 7} activity file IDs")

        val buffer = ByteBuffer.wrap(recordIds).order(ByteOrder.LITTLE_ENDIAN)
        val fileIds = mutableListOf<XiaomiActivityFileId>()

        while (buffer.position() < buffer.limit()) {
            val fileId = XiaomiActivityFileId.from(buffer)
            println("Got activity to fetch: $fileId")

            if (fileId.getTimestamp().time == 0L && fileId.getVersion() == 0) {
                println("Skipping invalid file with no timestamp and version")
                continue
            }

            fileIds.add(fileId)
        }

        activityFetcher.fetch(fileIds)

        if (subtype == CMD_ACTIVITY_FETCH_TODAY) {
            println("Fetch recorded data from the past")
            fetchRecordedDataPast()
        }
    }

    override fun handleCommand(cmd: XiaomiProto.Command) {
        when (cmd.subtype) {
            CMD_ACTIVITY_FETCH_PAST,
            CMD_ACTIVITY_FETCH_TODAY -> handleActivityFetchResponse(cmd.subtype, cmd.health.activityRequestFileIds.toByteArray())

            else -> println("Unknown health command ${cmd.subtype}")
        }
    }

    fun onFetchRecordedData(dataTypes: Int) {
        println("Fetch recorded data: ${String.format("0x%08X", dataTypes)}")
        fetchRecordedDataToday()
    }

    fun requestRecordedData(fileId: XiaomiActivityFileId) {
        support.sendCommand(
            "request recorded data",
            XiaomiProto.Command.newBuilder()
                .setType(COMMAND_TYPE)
                .setSubtype(CMD_ACTIVITY_FETCH_REQUEST)
                .setHealth(XiaomiProto.Health.newBuilder().setActivityRequestFileIds(
                    ByteString.copyFrom(fileId.toBytes())
                ))
                .build()
        )
    }

    private fun setHeartRateConfig() {
        val prefs = getWearablePreferences()

        val sleepDetection = prefs.getBoolean(WearablePreferenceConst.PREF_HEART_RATE_USE_FOR_SLEEP_DETECTION, true)
        val sleepBreathingQuality = prefs.getBoolean(WearablePreferenceConst.PREF_HEART_RATE_SLEEP_BREATHING_QUALITY_MONITORING, false)
        val intervalSeconds = prefs.getInt(WearablePreferenceConst.PREF_HEART_RATE_MEASUREMENT_INTERVAL, -1)
        val alertHighThreshold = prefs.getInt(WearablePreferenceConst.PREF_HEART_RATE_ALERT_HIGH_THRESHOLD, 0)
        val alertLowThreshold = prefs.getInt(WearablePreferenceConst.PREF_HEART_RATE_ALERT_LOW_THRESHOLD, 0)

        val intervalMin = if (intervalSeconds == -1) 0 else intervalSeconds / 60

        val heartRateBuilder = XiaomiProto.HeartRate.newBuilder()
            .setDisabled(intervalSeconds == 0)
            .setInterval(intervalMin)
            .setAdvancedMonitoring(XiaomiProto.AdvancedMonitoring.newBuilder().setEnabled(sleepDetection))
            .setBreathingScore(if (sleepBreathingQuality) 1 else 2)
            .setAlarmHighEnabled(alertHighThreshold > 0)
            .setAlarmHighThreshold(alertHighThreshold)
            .setHeartRateAlarmLow(
                XiaomiProto.HeartRateAlarmLow.newBuilder()
                    .setAlarmLowEnabled(alertLowThreshold > 0)
                    .setAlarmLowThreshold(alertLowThreshold)
            )
            .setUnknown7(1)

        support.sendCommand(
            "set heart rate config",
            XiaomiProto.Command.newBuilder()
                .setType(COMMAND_TYPE)
                .setSubtype(CMD_CONFIG_HEART_RATE_SET)
                .setHealth(XiaomiProto.Health.newBuilder().setHeartRate(heartRateBuilder))
                .build()
        )
    }

    private fun setSpo2Config() {
        val prefs = getWearablePreferences()

        val allDayMonitoring = prefs.getBoolean(WearablePreferenceConst.PREF_SPO2_ALL_DAY_MONITORING, true)
        val lowAlertThreshold = prefs.getInt(WearablePreferenceConst.PREF_SPO2_LOW_ALERT_THRESHOLD, 0)

        val spo2AlarmLowBuilder = XiaomiProto.Spo2AlarmLow.newBuilder()
            .setAlarmLowEnabled(lowAlertThreshold != 0)

        if (lowAlertThreshold != 0) spo2AlarmLowBuilder.setAlarmLowThreshold(lowAlertThreshold)

        val spo2Builder = XiaomiProto.SpO2.newBuilder()
            .setUnknown1(1)
            .setAllDayTracking(allDayMonitoring)
            .setAlarmLow(spo2AlarmLowBuilder)

        support.sendCommand(
            "set spo2 config",
            XiaomiProto.Command.newBuilder()
                .setType(COMMAND_TYPE)
                .setSubtype(CMD_CONFIG_SPO2_SET)
                .setHealth(XiaomiProto.Health.newBuilder().setSpo2(spo2Builder))
                .build()
        )
    }

    private fun setStressConfig() {
        val prefs = getWearablePreferences()

        val allDayMonitoring = prefs.getBoolean(WearablePreferenceConst.PREF_HEART_RATE_STRESS_MONITORING, true)
        val relaxReminder = prefs.getBoolean(WearablePreferenceConst.PREF_HEART_RATE_STRESS_RELAXATION_REMINDER, false)

        val stressBuilder = XiaomiProto.Stress.newBuilder()
            .setAllDayTracking(allDayMonitoring)
            .setRelaxReminder(
                XiaomiProto.RelaxReminder.newBuilder()
                    .setEnabled(relaxReminder)
                    .setUnknown2(0)
            )

        support.sendCommand(
            "set stress config",
            XiaomiProto.Command.newBuilder()
                .setType(COMMAND_TYPE)
                .setSubtype(CMD_CONFIG_STRESS_SET)
                .setHealth(XiaomiProto.Health.newBuilder().setStress(stressBuilder))
                .build()
        )
    }

    companion object {
        private const val CMD_ACTIVITY_FETCH_ACK = 5
        private const val CMD_ACTIVITY_FETCH_PAST = 2
        private const val CMD_ACTIVITY_FETCH_TODAY = 1
        private const val CMD_ACTIVITY_FETCH_REQUEST = 3

        private const val CMD_CONFIG_HEART_RATE_SET = 11
        private const val CMD_CONFIG_SPO2_SET = 9
        private const val CMD_CONFIG_STRESS_SET = 15

        const val COMMAND_TYPE = 8
    }
}