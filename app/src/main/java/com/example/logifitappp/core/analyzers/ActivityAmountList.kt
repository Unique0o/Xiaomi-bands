package com.example.logifitappp.core.analyzers

import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.core.wearebles.WearableActivityTypeEnum
import com.example.logifitappp.data.models.SleepModel
import kotlin.math.max

class ActivityAmountList {
    private val amounts = mutableListOf<ActivityAmount>()

    var maxAwakeningMinutes = 0L
    var remCycles = 0

    var totalAwakeningMinutes = 0L
    var totalDeepSleepMinutes = 0L
    var totalLightSleepMinutes = 0L
    var totalRemSleepMinutes = 0L
    var totalSleepMinutes = 0L

    var remSleepPercentage: Long = 0
        get() = if (totalSleepMinutes == 0L) 0 else totalRemSleepMinutes * 100 / totalSleepMinutes
        private set

    fun add(amount: ActivityAmount) {
        amounts.add(amount)
    }

    fun calculateMinutes() {
        var prevAmount: ActivityAmount? = null

        for (amount in amounts) {
            when (amount.activityType) {
                WearableActivityTypeEnum.REM_SLEEP -> amount.totalMinutes.let {
                    totalRemSleepMinutes += it
                    totalSleepMinutes += it

                    if (prevAmount != null && !prevAmount!!.isRemSleep()) remCycles++
                }

                WearableActivityTypeEnum.DEEP_SLEEP -> amount.totalMinutes.let {
                    totalDeepSleepMinutes += it
                    totalSleepMinutes += it
                }

                WearableActivityTypeEnum.LIGHT_SLEEP -> amount.totalMinutes.let {
                    totalLightSleepMinutes += it
                    totalSleepMinutes += it
                }

                else -> amount.totalMinutes.let {
                    totalAwakeningMinutes += it
                    maxAwakeningMinutes = max(maxAwakeningMinutes, it)
                }
            }

            prevAmount = amount
        }
    }

    fun getSleeps(wearableId: Int): List<SleepModel> {
        val sleeps = mutableListOf<SleepModel>()

        for (amount in amounts) {
            if (!amount.isSleep()) continue

            sleeps.add(SleepModel(
                deepSleepSeconds = if (amount.isDeepSleep()) amount.totalMinutes * 60 else 0,
                endAt = DateTimeUtils.formatExtendedIso8601(amount.endDate),
                interruptions = 0,
                lightSleepSeconds = if (amount.isLightSleep()) amount.totalMinutes * 60 else 0,
                remSleepSeconds = if (amount.isRemSleep()) amount.totalMinutes * 60 else 0,
                startAt = DateTimeUtils.formatExtendedIso8601(amount.startDate),
                totalSleepSeconds = totalSleepMinutes * 60,
                wearableId = wearableId
            ))
        }

        return sleeps
    }

    fun isEmpty() = amounts.isEmpty()

    fun lastOrNull() = amounts.lastOrNull()
}
