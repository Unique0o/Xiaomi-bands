package com.example.logifitappp.core.analyzers

import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.core.utils.MathUtils
import com.example.logifitappp.enums.WearableActivityTypeEnum
import com.example.logifitappp.data.models.SleepModel
import kotlin.math.abs
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
    var totalSteps = 0L

    var remSleepPercentage: Float = 0f
        get() = MathUtils.percentage(totalRemSleepMinutes, totalSleepMinutes)
        private set

    fun add(amount: ActivityAmount) {
        amounts.add(amount)
    }

    fun calculateInformation() {
        var prevAmount: ActivityAmount? = null

        for (amount in amounts) {
            when (amount.activityType) {
                WearableActivityTypeEnum.REM_SLEEP -> amount.totalMinutes.let {
                    totalRemSleepMinutes += it
                    totalSleepMinutes += it

                    if (prevAmount != null && !prevAmount!!.isRemSleep()) remCycles++

                    prevAmount = amount
                }

                WearableActivityTypeEnum.DEEP_SLEEP -> amount.totalMinutes.let {
                    totalDeepSleepMinutes += it
                    totalSleepMinutes += it
                    prevAmount = amount
                }

                WearableActivityTypeEnum.LIGHT_SLEEP -> amount.totalMinutes.let {
                    totalLightSleepMinutes += it
                    totalSleepMinutes += it
                    prevAmount = amount
                }

                else -> amount.totalMinutes.let {
                    if (prevAmount != null) {
                        totalAwakeningMinutes += it
                        maxAwakeningMinutes = max(maxAwakeningMinutes, it)
                        prevAmount = amount
                    }
                }
            }

            totalSteps += amount.totalSteps
        }
    }

    fun getAwakenings(): List<Awakening> {
        val sleeps = getSleeps(0)
        var prevSleep: SleepModel? = null

        val awakenings = mutableListOf<Awakening>()

        for (sleep in sleeps) {
            if (prevSleep != null) {
                val startAt = DateTimeUtils.parse(prevSleep.endAt, "yyyy-MM-dd HH:mm:ss")!!
                val endAt = DateTimeUtils.parse(sleep.startAt, "yyyy-MM-dd HH:mm:ss")!!

                awakenings.add(Awakening(
                    duration = abs(endAt.time - startAt.time) / 1000,
                    endAt = sleep.startAt,
                    startAt = prevSleep.endAt
                ))
            }

            prevSleep = sleep
        }

        return awakenings
    }

    fun getList() = amounts

    fun getSleeps(wearableId: Int): List<SleepModel> {
        val sleeps = mutableListOf<SleepModel>()
        var sleep: SleepModel? = null
        var interruptions = 0

        for (amount in amounts) {
            if (!amount.isSleep()) {
                sleep?.let {
                    sleeps.add(it)
                    interruptions++
                }

                sleep = null
                continue
            }

            sleep = sleep?.copy(
                deepSleepSeconds = sleep.deepSleepSeconds + if (amount.isDeepSleep()) amount.totalMinutes * 60 else 0,
                endAt = DateTimeUtils.formatExtendedIso8601(amount.endDate),
                lightSleepSeconds =  sleep.lightSleepSeconds + if (amount.isLightSleep()) amount.totalMinutes * 60 else 0,
                remSleepSeconds =  sleep.remSleepSeconds + if (amount.isRemSleep()) amount.totalMinutes * 60 else 0,
                totalSleepSeconds = sleep.totalSleepSeconds + amount.totalMinutes * 60,
            ) ?: SleepModel(
                deepSleepSeconds = if (amount.isDeepSleep()) amount.totalMinutes * 60 else 0,
                endAt = DateTimeUtils.formatExtendedIso8601(amount.endDate),
                interruptions = 0,
                lightSleepSeconds = if (amount.isLightSleep()) amount.totalMinutes * 60 else 0,
                remSleepSeconds = if (amount.isRemSleep()) amount.totalMinutes * 60 else 0,
                startAt = DateTimeUtils.formatExtendedIso8601(amount.startDate),
                totalSleepSeconds = amount.totalMinutes * 60,
                wearableId = wearableId
            )
        }

        sleep?.let { sleeps.add(it) }
        sleeps.forEach { it.interruptions = interruptions }

        return sleeps
    }

    fun isEmpty() = amounts.isEmpty()

    fun lastOrNull() = amounts.lastOrNull()

    override fun toString() = "activity amount list={" +
            "maxAwakeningMinutes: $maxAwakeningMinutes, " +
            "remCycles: $remCycles, " +
            "totalAwakeningMinutes: $totalAwakeningMinutes, " +
            "totalSleepMinutes: $totalSleepMinutes, " +
            "totalDeepSleepMinutes: $totalDeepSleepMinutes, " +
            "totalLightSleepMinutes: $totalLightSleepMinutes, " +
            "totalRemSleepMinutes: $totalRemSleepMinutes, " +
            "totalSteps: $totalSteps}"

    data class Awakening(val duration: Long, val endAt: String, val startAt: String)
}
