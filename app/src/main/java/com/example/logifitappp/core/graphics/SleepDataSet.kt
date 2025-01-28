package com.example.logifitappp.core.graphics

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.logifitappp.core.analyzers.ActivityAmount
import com.example.logifitappp.core.analyzers.ActivityAmountList
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.ui.theme.Zinc680
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class SleepDataSet(var amounts: ActivityAmountList) {
    var empty = amounts.totalSleepMinutes == 0L
        private set

    var self: BarDataSet
        private set

    var endTime: String
        private set

    var startTime: String
        private set

    var totalTime: Long
        private set

    init {
        var time = 0L
        val sleeps = mutableListOf<ActivityAmount>()

        if (!empty) {
            amounts.getList().let { list ->
                val startIndex = list.indexOfFirst { it.isSleep() }
                val endIndex = list.indexOfLast { it.isSleep() }

                if (startIndex != -1 && endIndex != -1) {
                    for (i in startIndex .. endIndex) {
                        time += list[i].totalMinutes
                        sleeps.add(list[i])
                    }
                }
            }
        } else time = 4L

        val values = if (empty) floatArrayOf(1f, 2f, 1f) else sleeps.map { it.totalMinutes.toFloat() }.toFloatArray()
        val entry = BarEntry(0f, values)

        val colors = if (empty) listOf(Zinc680.copy(alpha = 0.1f).toArgb()) else sleeps.map { it.activityType.color.toArgb() }

        self = BarDataSet(arrayListOf(entry), "Sleep data set").apply {
            setColors(colors)
            barBorderWidth = 2f
            barBorderColor = Color.Transparent.toArgb()
        }

        totalTime = time

        when {
            empty -> {
                endTime = "-"
                startTime = "-"
            }

            else -> {
                endTime = sleeps.lastOrNull()?.let {
                    DateTimeUtils.format(it.endDate, "HH:mm")
                } ?: "-"

                startTime = sleeps.firstOrNull()?.let {
                    DateTimeUtils.format(it.startDate, "HH:mm")
                } ?: "-"
            }
        }
    }
}