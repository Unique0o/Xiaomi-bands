package com.example.logifitappp.core.wearebles.huami.operations

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import com.example.logifitappp.R
import com.example.logifitappp.core.wearebles.AbstractRepeatingFetchOperation
import com.example.logifitappp.core.wearebles.huami.HuamiFetchDataTypeEnum
import com.example.logifitappp.core.wearebles.huami.HuamiSupport
import com.example.logifitappp.data.models.HuamiExtendedRawActivityModel

open class HuamiFetchActivityOperation(support: HuamiSupport): AbstractRepeatingFetchOperation(support, HuamiFetchDataTypeEnum.ACTIVITY) {
    private var sampleSize = support.getRawActivitySize()

    private fun createActivity(payload: ByteArray, i: Int) = HuamiExtendedRawActivityModel(
        type = payload[i].toInt() and 0xff,
        intensity = payload[i + 1].toInt() and 0xff,
        steps = payload[i + 2].toInt() and 0xff,
        heartRate = payload[i + 3].toInt() and 0xff,
    )

    private fun createExtendedActivity(payload: ByteArray, i: Int) = HuamiExtendedRawActivityModel(
        type = payload[i].toInt() and 0xff,
        intensity = payload[i + 1].toInt() and 0xff,
        steps = payload[i + 2].toInt() and 0xff,
        heartRate = payload[i + 3].toInt() and 0xff,
        unknown1 = payload[i + 4].toInt() and 0xff,
        sleep = payload[i + 5].toInt() and 0xff,
        deepSleep = payload[i + 6].toInt() and 0xff,
        remSleep = payload[i + 7].toInt() and 0xff,
    )

    override fun handleActivityData(timestamp: GregorianCalendar, bytes: ByteArray): Boolean {
        val coordinator = getSupport().getCoordinator()

        if (bytes.size % sampleSize != 0) return false.also { println("Unexpected ${getName()} array size: ${bytes.size}, sample: $sampleSize") }

        val samples = mutableListOf<HuamiExtendedRawActivityModel>()

        for (i in bytes.indices step sampleSize) {
            val sample = when (sampleSize) {
                4 -> createActivity(bytes, i)
                8 -> createExtendedActivity(bytes, i)
                else -> throw IllegalStateException("Unsupported sample size $sampleSize")
            }

            samples.add(sample)
        }

        if (samples.isEmpty()) return true.also { println("No samples to save") }

        println("Samples to save: ${samples.size}")

        try {
            val activityProvider = coordinator.getActivityProvider(wearable)

            for (sample in samples) {
                sample.timestamp = timestamp.timeInMillis / 1000
                timestamp.add(Calendar.MINUTE, 1)
            }

            activityProvider.store(*samples.toTypedArray())
            timestamp.add(Calendar.MINUTE, -1)

            return true.also { println("Huami activity data: last sample timestamp: ${timestamp.time}") }
        } catch (e: Exception) {
            return false.also { println("Error saving activity samples $e") }
        }
    }

    override fun getLastSyncTimeKey() = "lastSyncTimeMillis"

    override fun taskDescription() = context.getString(R.string.busy_task_fetch_activity_data)

    override fun validChecksum(crc32: Int): Boolean {
        return true.also { println("Checksum not implemented for activity data, assuming it's valid") }
    }
}