package com.example.logifitappp.core.analyzers

import android.icu.util.Calendar
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.data.models.commons.WearableRawActivityModel

class HeartRateAnalyzer {
    fun calculate(activities: List<WearableRawActivityModel>, calendar: Calendar, rangeInMinutes: Long): HeartRateAmountList {
        val amounts = HeartRateAmountList()
        var totalMeasuredHeartRate = 0L
        var measuredHeartRateTimes = 0

        val timestamps = DateTimeUtils.getStartOfTodayAndTomorrow(calendar)
        val rangeInMills = rangeInMinutes * 60 * 1000

        for (i in timestamps.first until  timestamps.second step rangeInMills) {
            val startTime = i / 1000
            val endTime = (i + rangeInMills) / 1000

            val filteredActivities = activities.filter { it.timestamp in startTime ..< endTime }

            amounts.add(HeartRateAmount().apply {
                setStartDate(startTime)
                setEndDate(endTime)

                for (activity in filteredActivities) {
                    if (!activity.isHeartRateValid()) continue

                    setHeartRate(activity.heartRate)
                    amounts.latestMeasuredHeartRate = activity.heartRate.toLong()
                    totalMeasuredHeartRate += activity.heartRate
                    measuredHeartRateTimes++
                }

                if (maxHeartRate == 0L) minHeartRate = 0L
            })
        }

        if (measuredHeartRateTimes > 0) amounts.averageHeartRate = totalMeasuredHeartRate / measuredHeartRateTimes.toFloat()

        if (amounts.maxMeasuredHeartRate == 0L) amounts.minMeasuredHeartRate = 0L

        return amounts
    }
}