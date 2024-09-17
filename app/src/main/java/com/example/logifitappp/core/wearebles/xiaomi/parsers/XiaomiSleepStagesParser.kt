package com.example.logifitappp.core.wearebles.xiaomi.parsers

import com.example.logifitappp.core.wearebles.xiaomi.XiaomiActivityFileId
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiSleepStageProvider
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiSleepTimeProvider
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiSupport
import com.example.logifitappp.data.models.XiaomiSleepStageModel
import com.example.logifitappp.data.models.XiaomiSleepTimeModel
import java.nio.ByteBuffer
import java.nio.ByteOrder

class XiaomiSleepStagesParser: XiaomiActivityParser() {
    override fun parse(support: XiaomiSupport, fileId: XiaomiActivityFileId, bytes: ByteArray): Boolean {
        if (fileId.getVersion() != 2) return false.also { println("Unknown sleep stages version ${fileId.getVersion()}") }

        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
        buffer.get(ByteArray(7))
        val fileIdPadding = buffer.get()

        if (fileIdPadding != 0.toByte()) println("Expected 0 padding after fileId, got $fileIdPadding - parsing might fail")

        buffer.get(ByteArray(7))
        val sleepDuration = buffer.getShort()
        val bedTime = buffer.getInt()
        val wakeupTime = buffer.getInt()

        buffer.get(ByteArray(3))
        val deepSleepDuration = buffer.getShort()
        val lightSleepDuration = buffer.getShort()
        val remSleepDuration = buffer.getShort()
        val wakeDuration = buffer.getShort()

        println("Sleep stages sample: bedTime: $bedTime, wakeupTime: $wakeupTime, sleepDuration: $sleepDuration")

        if (bedTime == 0 || wakeupTime == 0 || sleepDuration == 0.toShort()) {
            return true.also { println("Ignoring sleep stages sample with no data") }
        }

        val time = XiaomiSleepTimeModel()
        time.timestamp = bedTime * 1000L
        time.wakeupTime = wakeupTime * 1000L
        time.isAwake = false
        time.totalDuration = sleepDuration.toInt()
        time.deepSleepDuration = deepSleepDuration.toInt()
        time.lightSleepDuration = lightSleepDuration.toInt()
        time.remSleepDuration = remSleepDuration.toInt()
        time.awakeDuration = wakeDuration.toInt()

        val stages = mutableListOf<XiaomiSleepStageModel>()
        buffer.get()

        while (buffer.position() < buffer.limit()) {
            val t = buffer.getInt()
            val phase = buffer.get().toInt() and 0xff

            val stage = XiaomiSleepStageModel()
            stage.timestamp = t * 1000L
            stage.stage = phase
            stages.add(stage)
        }

        val wearable = support.getWearable()

        try {
            val sleepTimeProvider = XiaomiSleepTimeProvider(wearable)
            val existingTimes = sleepTimeProvider.getBetween(time.timestamp, time.timestamp)

            if (existingTimes.isNotEmpty()) {
                val existingTime = existingTimes[0]

                if (existingTime.wakeupTime!! > time.wakeupTime!!) {
                    return true.also { println("Ignoring sleep sample - existing sample is more recent (${existingTime.wakeupTime})") }
                }
            }

            sleepTimeProvider.store(time)
        } catch (e: Exception) {
            return false.also { println("Error saving sleep sample $e") }
        }

        try {
            val sleepStageProvider = XiaomiSleepStageProvider(wearable)
            sleepStageProvider.store(*stages.toTypedArray())
        } catch (e: Exception) {
            return false.also { println("Error saving sleep stage samples $e") }
        }

        return true
    }
}