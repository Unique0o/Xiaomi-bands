package com.example.logifitappp.core.analyzers

import android.icu.util.Calendar
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.data.models.commons.WearableRawActivityModel

class StepsAnalyzer {
    fun calculate(activities: List<WearableRawActivityModel>, calendar: Calendar, rangeInMinutes: Long): StepsAmountList {
        val amounts = StepsAmountList()
        val timestamps = DateTimeUtils.getStartOfTodayAndTomorrow(calendar)
        val rangeInMills = rangeInMinutes * 60 * 1000

        for (i in timestamps.first until  timestamps.second step rangeInMills) {
            val startTime = i / 1000
            val endTime = (i + rangeInMills) / 1000

            val filteredActivities = activities.filter { it.timestamp in startTime ..< endTime }

            amounts.add(StepsAmount().apply {
                setStartDate(startTime)
                setEndDate(endTime)

                for (activity in filteredActivities) addSteps(activity.steps)
            })
        }

        return amounts
    }
}