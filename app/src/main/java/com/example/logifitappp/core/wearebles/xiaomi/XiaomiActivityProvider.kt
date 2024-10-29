package com.example.logifitappp.core.wearebles.xiaomi

import com.example.logifitappp.core.App
import com.example.logifitappp.core.utils.RangeMap
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableActivityProvider
import com.example.logifitappp.core.wearebles.WearableActivityTypeEnum
import com.example.logifitappp.data.models.XiaomiRawActivityModel
import com.example.logifitappp.data.models.XiaomiSleepStageModel

class XiaomiActivityProvider(private val wearable: Wearable): WearableActivityProvider<XiaomiRawActivityModel>(wearable) {
    override fun getRawActivitiesBetween(from: Long, to: Long): List<XiaomiRawActivityModel> {
        val activities = super.getRawActivitiesBetween(from, to)

        overlaySleep(activities, from, to)

        return activities
    }

    private fun getWearableActivityTypeForStage(model: XiaomiSleepStageModel) = when (model.stage) {
        2 -> WearableActivityTypeEnum.DEEP_SLEEP
        3 -> WearableActivityTypeEnum.LIGHT_SLEEP
        4 -> WearableActivityTypeEnum.DEEP_SLEEP
        else -> WearableActivityTypeEnum.UNKNOWN
    }

    override fun getWearableRawActivityDao() = App.database.xiaomiRawActivityDao()

    override fun normalizeIntensity(intensity: Int) = intensity / 100f

    override fun normalizeType(type: Int) = WearableActivityTypeEnum.fromCode(type)

    private fun overlaySleep(activities: List<XiaomiRawActivityModel>, from: Long, to: Long) {
        val stagesMap = RangeMap<Long, WearableActivityTypeEnum>()

        val sleepTimeSampleProvider = XiaomiSleepTimeProvider(wearable)
        val sleepStageProvider = XiaomiSleepStageProvider(wearable)

        val sleepTimesWithinRange = sleepTimeSampleProvider.getBetween(from * 1000L, to * 1000L)
        println("Found ${sleepTimesWithinRange.size} sleep samples between $from to $to")

        sleepTimesWithinRange.forEach {
            stagesMap.put(it.wakeupTime!!.toLong(), WearableActivityTypeEnum.UNKNOWN)
            stagesMap.put(it.timestamp, WearableActivityTypeEnum.LIGHT_SLEEP)
        }

        val lastSleepStageBeforeRange = sleepStageProvider.getLastBeforeOf(from * 1000L)

        if (lastSleepStageBeforeRange != null) {
            println("Last sleep stage before range: ts=${lastSleepStageBeforeRange.timestamp}, stage=${lastSleepStageBeforeRange.stage}")
            stagesMap.put(lastSleepStageBeforeRange.timestamp, getWearableActivityTypeForStage(lastSleepStageBeforeRange))
        }

        val sleepStagesInRange = sleepStageProvider.getBetween(from * 1000L, to * 1000L)

        if (sleepStagesInRange.isNotEmpty()) {
            println("Found ${sleepStagesInRange.size} sleep stage samples between $from and $to")

            sleepStagesInRange.forEach {
                stagesMap.put(it.timestamp, getWearableActivityTypeForStage(it))
            }
        }

        val lastSleepTimesBeforeRange = sleepTimeSampleProvider.getLastBeforeOf(from * 1000L)

        if (lastSleepTimesBeforeRange != null) {
            println("Last sleep time before range: ts=${lastSleepTimesBeforeRange.timestamp}, stage=$lastSleepTimesBeforeRange")

            stagesMap.put(lastSleepTimesBeforeRange.timestamp, WearableActivityTypeEnum.UNKNOWN)
            stagesMap.put(lastSleepTimesBeforeRange.timestamp, WearableActivityTypeEnum.LIGHT_SLEEP)
        }

        val sleepTimesInRange = sleepTimeSampleProvider.getBetween(from * 1000L, to * 1000L)

        if (sleepTimesInRange.isNotEmpty()) {
            println("Found ${sleepTimesInRange.size} sleep samples between $from and $to")

            sleepTimesInRange.forEach {
                if (sleepStagesInRange.isEmpty()) stagesMap.put(it.timestamp, WearableActivityTypeEnum.LIGHT_SLEEP)

                stagesMap.put(it.wakeupTime!!.toLong(), WearableActivityTypeEnum.UNKNOWN)
            }
        }

        if (!stagesMap.isEmpty()) {
            println("Found ${stagesMap.size()} sleep samples between $from and $to")

            activities.forEach {
                val ts = it.timestamp * 1000L
                val stage = stagesMap.get(ts)

                if (stage != null && stage != WearableActivityTypeEnum.UNKNOWN) {
                    it.type = stage.getCode()

                    when (stage) {
                        WearableActivityTypeEnum.DEEP_SLEEP -> it.intensity = 20
                        WearableActivityTypeEnum.LIGHT_SLEEP -> it.intensity = 30
                        WearableActivityTypeEnum.REM_SLEEP -> it.intensity = 40
                        else -> {}
                    }
                }
            }
        }
    }
}