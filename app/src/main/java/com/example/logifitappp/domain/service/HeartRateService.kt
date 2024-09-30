package com.example.logifitappp.domain.service


import com.example.logifitappp.data.models.HeartRateFragmentGroupType
import com.example.logifitappp.data.models.commons.WearableRawActivityModel
import com.example.logifitappp.enums.ActivityTypeEnum
import com.example.logifitappp.enums.HeartRateTypeEnum
import java.time.LocalDateTime
import java.time.ZoneOffset

class HeartRateService {

    suspend fun calculateAverageHeartRate(activities: List<WearableRawActivityModel>): Double {
        var totalMeasuredHeartRate = 0
        var measuredHeartRateCount = 0

        for (activity in activities) {
            if (!isHeartRateValueValid(activity.heartRate)) continue

            totalMeasuredHeartRate += activity.heartRate
            measuredHeartRateCount++
        }

        return if (measuredHeartRateCount == 0) 0.0 else totalMeasuredHeartRate.toDouble() / measuredHeartRateCount
    }


    private fun isHeartRateValueValid(value: Int): Boolean {
        return value in 10..250
    }

    fun getMinMaxAndLatestByFragments(fragments: HeartRateFragmentGroupType): Triple<Int, Int, Int> {
        var max = Int.MIN_VALUE
        var min = Int.MAX_VALUE
        var latest = 0

        for (fragment in fragments.values) {
            max = maxOf(max, fragment.max)
            min = minOf(min, fragment.min)
            if (fragment.latest != 0) latest = fragment.latest
        }

        if (max == 0) min = 0

        return Triple(min, max, latest)
    }

    fun getMeasureLevelToLpm(lpm: Int): HeartRateTypeEnum {
        return when {
            lpm <= 100 -> HeartRateTypeEnum.HEART_RATE_REPOSE
            lpm in 101..114 -> HeartRateTypeEnum.HEART_RATE_VERY_SOFT
            lpm in 115..133 -> HeartRateTypeEnum.HEART_RATE_SMOOTH
            lpm in 134..152 -> HeartRateTypeEnum.HEART_RATE_MODERATE
            lpm in 153..172 -> HeartRateTypeEnum.HEART_RATE_INTENSE
            lpm in 173..255 -> HeartRateTypeEnum.HEART_RATE_MAXIMUM
            else -> HeartRateTypeEnum.HEART_RATE_UNKNOWN
        }
    }

    private fun getStartOfTodayAndTomorrow(date: LocalDateTime): Pair<Long, Long> {
        val startOfDay = date.toLocalDate().atStartOfDay().toEpochSecond(ZoneOffset.UTC)
        val startOfNextDay = startOfDay + 86400 // 24 hours in seconds
        return Pair(startOfDay, startOfNextDay)
    }
}
