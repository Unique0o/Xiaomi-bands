package com.example.logifitappp.core.analyzers

import android.icu.util.Calendar
import com.example.logifitappp.data.models.commons.WearableRawActivityModel

class StepsAnalyzer {
    fun calculate(activities: List<WearableRawActivityModel>, calendar: Calendar, rangeInMinutes: Long): StepsAmountList {
        val amounts = StepsAmountList()

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

            amounts.add(StepsAmount().apply {
                setStartDate(startTime)
                setEndDate(endTime)

                for (activity in filteredActivities) addSteps(activity.steps)
            })
        }

        return amounts
    }
}