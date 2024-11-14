package com.example.logifitappp.core.analyzers

import android.icu.util.Calendar
import com.example.logifitappp.data.models.commons.WearableRawActivityModel

class HeartRateAnalyzer {
    fun calculate(activities: List<WearableRawActivityModel>, calendar: Calendar, rangeInMinutes: Long): HeartRateAmountList {
        val amounts = HeartRateAmountList()
        var totalMeasuredHeartRate = 0L
        var measuredHeartRateTimes = 0

        val initDay = calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis / 1000

        for (j in 0 .. 47) {
            val rangeInMillis = rangeInMinutes * 60

            val startTime = initDay + j * rangeInMillis
            val endTime = startTime + rangeInMillis

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

                println("hear rate amount: $this")
            })
        }

        if (measuredHeartRateTimes > 0) amounts.averageHeartRate = totalMeasuredHeartRate / measuredHeartRateTimes.toFloat()

        if (amounts.maxMeasuredHeartRate == 0L) amounts.minMeasuredHeartRate = 0L

        return amounts
    }
}