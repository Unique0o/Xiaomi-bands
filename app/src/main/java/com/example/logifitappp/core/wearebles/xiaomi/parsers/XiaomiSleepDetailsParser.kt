package com.example.logifitappp.core.wearebles.xiaomi.parsers

import com.example.logifitappp.core.wearebles.xiaomi.XiaomiActivityFileId
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiSleepStageProvider
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiSleepTimeProvider
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiSupport
import com.example.logifitappp.data.models.XiaomiSleepStageModel
import com.example.logifitappp.data.models.XiaomiSleepTimeModel
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.abs

class XiaomiSleepDetailsParser: XiaomiActivityParser() {
    override fun parse(support: XiaomiSupport, fileId: XiaomiActivityFileId, bytes: ByteArray): Boolean {
        if (fileId.getVersion() > 4) return false.also { println("Unknown sleep details version ${fileId.getVersion()}") }

        var versionDependentFields = 0

        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
        buffer.get(ByteArray(7))
        val fileIdPadding = buffer.get()

        if (fileIdPadding != 0.toByte()) println("Expected 0 padding after fileId, got $fileIdPadding - parsing might fail")

        val header = buffer.get().toInt()
        val isAwake = buffer.get().toInt() and 0xff
        val bedTime = buffer.getInt()
        val wakeupTime = buffer.getInt()

        if (fileId.getVersion() >= 4) {
            versionDependentFields += 1
            buffer.get().toInt() and 0xff
        }

        var lastHeartPulseTimestamp = 0L
        println("Sleep sample: bedTime: $bedTime, wakeupTime: $wakeupTime, isAwake: $isAwake")

        var time: XiaomiSleepTimeModel? = XiaomiSleepTimeModel()
        time!!.timestamp = bedTime * 1000L
        time.wakeupTime = wakeupTime * 1000L
        time.isAwake = isAwake == 1

        if ((header and (1 shl (5 - versionDependentFields))) != 0) {
            println("Heart rate samples from offset ${buffer.position()}")

            buffer.getShort()
            val count = buffer.getShort()

            if (count > 0) {
                if (fileId.getVersion() >= 2) buffer.getInt()

                buffer.position(buffer.position() + count)
            }
        }

        if ((header and (1 shl (4 - versionDependentFields))) != 0) {
            println("SpO2 samples from offset ${buffer.position()}")

            buffer.getShort()
            val count = buffer.getShort()

            if (count > 0) {
                if (fileId.getVersion() >= 2) buffer.getInt()

                buffer.position(buffer.position() + count)
            }
        }

        if (fileId.getVersion() >= 3 && (header and (1 shl (3 - versionDependentFields))) != 0) {
            println("Snore level samples from offset ${buffer.position()}")

            buffer.getShort()
            val count = buffer.getShort()

            if (count > 0) {
                if (fileId.getVersion() >= 2) buffer.getInt()

                buffer.position(buffer.position() + count * 4)
            }
        }

        val times = mutableListOf<XiaomiSleepTimeModel>()
        val stages = mutableListOf<XiaomiSleepStageModel>()
        var stagesParseFailed = false.also { println("Sleep stage packets from offset ${buffer.position()}") }

        try {
            while (buffer.remaining() >= 17) {
                if (!readStagePacketHeader(buffer)) break

                buffer.get().toInt() and 0xff

                val ts = buffer.getLong()
                buffer.get().toInt() and 0xff
                val type = buffer.get().toInt() and 0xff
                val dataLength = ((buffer.get().toInt() and 0xff) shl 8) or (buffer.get().toInt() and 0xff)

                if (type == 0x2 || type == 0x3 || type == 0x9 || type == 0xc || type == 0xd || type == 0xe || type == 0xf) continue

                val data = ByteArray(dataLength)
                buffer.get(data)

                val dataBuffer = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN)

                when (type) {
                    1 -> {
                        if (abs(lastHeartPulseTimestamp - ts) > 30000) {
                            lastHeartPulseTimestamp = ts
                            //TODO: heart pulse when is required
                        }

                        while (dataBuffer.position() < dataBuffer.limit()) {
                            val delta = dataBuffer.get().toInt() and 0xff
                            lastHeartPulseTimestamp += 10 * delta
                            //TODO: heart pulse when is required
                        }
                    }

                    16 -> {
                        dataBuffer.get().toInt() and 0xff

                        val sleepDuration = dataBuffer.getShort().toInt() and 0xffff
                        val wakeDuration = dataBuffer.getShort().toInt() and 0xffff
                        val lightSleepDuration = dataBuffer.getShort().toInt() and 0xffff
                        val remSleepDuration = dataBuffer.getShort().toInt() and 0xffff
                        val deepSleepDuration = dataBuffer.getShort().toInt() and 0xffff

                        dataBuffer.get().toInt() and 0xff
                        dataBuffer.get().toInt() and 0xff

                        if (time == null) time = XiaomiSleepTimeModel()

                        time.timestamp = bedTime * 1000L
                        time.wakeupTime = wakeupTime * 1000L
                        time.totalDuration = sleepDuration
                        time.deepSleepDuration = deepSleepDuration
                        time.lightSleepDuration = lightSleepDuration
                        time.remSleepDuration = remSleepDuration
                        time.awakeDuration = wakeDuration
                        times.add(time)

                        time = null
                    }

                    17 -> {
                        var currentTime = ts * 1000

                        for (i in 0 until dataLength / 2) {
                            val value = dataBuffer.getShort().toInt() and 0xffff
                            val phase = value shr 12
                            val offsetMinutes = value and 0xfff

                            val stage = XiaomiSleepStageModel()
                            stage.timestamp = currentTime
                            stage.stage = decodeStage(phase)
                            stages.add(stage)

                            currentTime += offsetMinutes * 60000
                        }
                    }
                }
            }
        } catch (e: BufferUnderflowException) {
            stagesParseFailed = true.also { println("Buffer underflow while parsing sleep stages... $e") }
        }

        if (times.isEmpty()) times.add(time!!)

        var persistSuccess = !stagesParseFailed
        val wearable = support.getWearable()

        try {
            val sleepTimeProvider = XiaomiSleepTimeProvider(wearable)

            for (tm in times) {
                val existingTimes = sleepTimeProvider.getBetween(tm.timestamp, tm.timestamp)

                if (existingTimes.isNotEmpty()) {
                    val existingTime = existingTimes[0]

                    if (existingTime.wakeupTime!! > tm.wakeupTime!!) {
                        println("Ignoring sleep sample - existing sample is more recent (${existingTime.wakeupTime})")
                        continue
                    }
                }

                sleepTimeProvider.store(tm)
            }
        } catch (e: Exception) {
            persistSuccess = false.also { println("Error saving sleep sample $e") }
        }

        if (!stagesParseFailed && stages.isNotEmpty()) {
            println("Persisting ${stages.size} sleep stage samples")

            try {
                val sleepStageProvider = XiaomiSleepStageProvider(wearable)
                sleepStageProvider.store(*stages.toTypedArray())
            } catch (e: Exception) {
                persistSuccess = false.also { println("Error saving sleep stage samples $e") }
            }
        }

        return persistSuccess
    }

    companion object {
        private fun decodeStage(stage: Int) = when (stage) {
            0 -> 5
            1 -> 3
            2 -> 2
            3 -> 4
            4 -> 0
            else -> 1
        }

        private fun readStagePacketHeader(buffer: ByteBuffer): Boolean {
            while (buffer.remaining() >= 17) {
                if (buffer.getInt() != 0xfffcfafb.toInt()) {
                    buffer.position(buffer.position() - 3)
                    continue
                }

                return true
            }

            return false
        }
    }
}