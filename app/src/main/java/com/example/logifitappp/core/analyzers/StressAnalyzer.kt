package com.example.logifitappp.core.analyzers

import android.icu.util.Calendar
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.data.models.commons.WearableStressSampleModel

class StressAnalyzer {
    fun calculate(samples: List<WearableStressSampleModel>, calendar: Calendar, rangeInMinutes: Long): StressAmountList {
        val amounts = StressAmountList()
        var totalMeasuredStress = 0
        var measuredStressTimes = 0

        val timestamps = DateTimeUtils.getStartOfTodayAndTomorrow(calendar)
        val rangeInMills = rangeInMinutes * 60 * 1000

        for (startTime in timestamps.first until  timestamps.second step rangeInMills) {
            val endTime = startTime + rangeInMills

            val filteredSamples = samples.filter { it.timestamp in startTime..<endTime }

            amounts.add(StressAmount().apply {
                setStartDate(startTime)
                setEndDate(endTime)

                for (sample in filteredSamples) {
                    setStress(sample.stress)

                    amounts.latestMeasuredStress = sample.stress
                    totalMeasuredStress += sample.stress
                    measuredStressTimes++
                }

                if (maxStress == 0) minStress = 0
            })
        }

        if (measuredStressTimes > 0) amounts.averageStress = totalMeasuredStress / measuredStressTimes.toFloat()

        if (amounts.maxMeasuredStress == 0) amounts.minMeasuredStress = 0

        return amounts
    }
}