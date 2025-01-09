package com.example.logifitappp.core.analyzers

import android.icu.util.Calendar
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.data.models.commons.WearableSpo2SampleModel
import com.example.logifitappp.enums.Spo2ModeEnum

class Spo2Analyzer {
    fun calculate(samples: List<WearableSpo2SampleModel>, calendar: Calendar, rangeInMinutes: Long): Spo2AmountList {
        val amounts = Spo2AmountList()
        var totalMeasuredSpo2 = 0
        var measuredSpo2Times = 0

        val timestamps = DateTimeUtils.getStartOfTodayAndTomorrow(calendar)
        val rangeInMills = rangeInMinutes * 60 * 1000

        for (startTime in timestamps.first until  timestamps.second step rangeInMills) {
            val endTime = startTime + rangeInMills

            val filteredSamples = samples.filter { it.timestamp in startTime..<endTime }

            amounts.add(Spo2Amount().apply {
                setStartDate(startTime)
                setEndDate(endTime)

                for (sample in filteredSamples) {
                    addMode(Spo2ModeEnum.fromName(sample.modeName))
                    setSpo2(sample.spo2)

                    amounts.latestMeasuredSpo2 = sample.spo2
                    totalMeasuredSpo2 += sample.spo2
                    measuredSpo2Times++
                }

                if (maxSpo2 == 0) minSpo2 = 0
            })
        }

        if (measuredSpo2Times > 0) amounts.averageSpo2 = totalMeasuredSpo2 / measuredSpo2Times.toFloat()

        if (amounts.maxMeasuredSpo2 == 0) amounts.minMeasuredSpo2 = 0

        return amounts
    }
}