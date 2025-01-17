package com.example.logifitappp.core.wearebles.huami.operations

import android.icu.util.GregorianCalendar
import com.example.logifitappp.R
import com.example.logifitappp.core.analyzers.ActivityAnalyzer
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.core.wearebles.AbstractRepeatingFetchOperation
import com.example.logifitappp.core.wearebles.huami.HuamiFetchDataTypeEnum
import com.example.logifitappp.core.wearebles.huami.HuamiSupport
import com.example.logifitappp.data.models.HuamiSpo2SampleModel
import com.example.logifitappp.enums.Spo2ModeEnum
import com.example.logifitappp.enums.Spo2TypeEnum
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.max
import kotlin.math.min

open class HuamiFetchSpo2NormalOperation(support: HuamiSupport): AbstractRepeatingFetchOperation(support, HuamiFetchDataTypeEnum.SPO2_NORMAL) {
    override fun getLastSyncTimeKey() = "lastSpo2normalTimeMillis"

    override fun handleActivityData(timestamp: GregorianCalendar, bytes: ByteArray): Boolean {
        if ((bytes.size - 1) % 65 != 0) {
            return false.also { println("Unexpected length for spo2 data ${bytes.size}, not divisible by 65") }
        }

        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
        val version = buffer.get().toInt() and 0xff

        if (version != 2) {
            return false.also { println("Unknown normal spo2 data version $version") }
        }

        val samples = mutableListOf<HuamiSpo2SampleModel>()

        var minTimestamp = Long.MAX_VALUE
        var maxTimestamp = 0L

        while (buffer.position() < bytes.size) {
            val timestampSeconds = buffer.getInt()
            val spo2Raw = buffer.get()
            val autoMeasurement = spo2Raw < 0
            val spo2 = (if (spo2Raw < 0) spo2Raw + 128 else spo2Raw).toByte()

            buffer.get(ByteArray(60))

            timestamp.timeInMillis = timestampSeconds * 1000L

            maxTimestamp = max(maxTimestamp, timestamp.timeInMillis)
            minTimestamp = min(minTimestamp, timestamp.timeInMillis)

            println("SPO2 at ${timestamp.timeInMillis}: $spo2 auto = $autoMeasurement")

            samples.add(HuamiSpo2SampleModel(
                spo2 = spo2.toInt(),
                typeNum = if (autoMeasurement) Spo2TypeEnum.AUTOMATIC.num else Spo2TypeEnum.MANUAL.num,
                timestamp = timestamp.timeInMillis
            ))
        }

        if (samples.isNotEmpty()) {
            minTimestamp -= (minTimestamp % (60 * 1000))
            maxTimestamp += (maxTimestamp % (60 * 1000))

            val activityProvider = getSupport().getCoordinator().getActivityProvider(wearable)
            val activities = activityProvider.getRawActivitiesBetween(minTimestamp / 1000, maxTimestamp / 1000)
            val amounts = ActivityAnalyzer().calculate(activities)
            val sleeps = amounts.getSleeps(0)

            samples.forEach { sample ->
                val sleep = sleeps.find {
                    val startAt = DateTimeUtils.parse(it.startAt, "yyyy-MM-dd HH:mm:ss")!!.time
                    val endAt = DateTimeUtils.parse(it.endAt, "yyyy-MM-dd HH:mm:ss")!!.time

                    sample.timestamp in startAt..endAt
                }

                if (sleep != null) sample.modeName = Spo2ModeEnum.ASLEEP.name
            }
        }

        return persistsSamples(samples)
    }

    private fun persistsSamples(samples: List<HuamiSpo2SampleModel>): Boolean {
        try {
            getSupport()
                .getCoordinator()
                .getSpo2SampleProvider(wearable)
                .store(*samples.toTypedArray())
        } catch (e: Exception) {
            return false.also { println("Error saving normal spo2 samples: $e") }
        }

        return true
    }

    override fun taskDescription() = context.getString(R.string.busy_task_fetch_spo2_data)
}