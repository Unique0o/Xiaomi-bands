package com.example.logifitappp.core.analyzers

import com.example.logifitappp.core.wearebles.WearableActivityTypeEnum
import java.util.Date

class ActivityAmount(val activityType: WearableActivityTypeEnum) {
    lateinit var endDate: Date
    lateinit var startDate: Date
    var totalMinutes: Long = 0

    fun addSeconds(seconds: Long) {
        totalMinutes += seconds / 60
    }

    fun isDeepSleep() = activityType == WearableActivityTypeEnum.DEEP_SLEEP

    fun isLightSleep() = activityType == WearableActivityTypeEnum.LIGHT_SLEEP

    fun isRemSleep() = activityType == WearableActivityTypeEnum.REM_SLEEP

    fun isSleep() = when (activityType) {
        WearableActivityTypeEnum.REM_SLEEP,
        WearableActivityTypeEnum.DEEP_SLEEP,
        WearableActivityTypeEnum.LIGHT_SLEEP -> true

        else -> false
    }

    fun setEndDate(seconds: Long) {
        this.endDate = Date(seconds * 1000)
    }

    fun setStartDate(seconds: Long) {
        this.startDate = Date(seconds * 1000)
    }
}