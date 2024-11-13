package com.example.logifitappp.core.graphics

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.logifitappp.core.analyzers.ActivityAmountList
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.ui.theme.Zinc680
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class SleepBarDataSet(amounts: ActivityAmountList) {
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
        val values = if (empty) floatArrayOf(1f, 2f, 1f) else amounts.getList().map { it.totalMinutes.toFloat() }.toFloatArray()
        val entry = BarEntry(0f, values)

        val colors = if (empty) listOf(Zinc680.copy(alpha = 0.1f).toArgb()) else amounts.getList().map { it.activityType.color.toArgb() }

        self = BarDataSet(arrayListOf(entry), "Sleep data set").apply {
            setColors(colors)
            barBorderWidth = 2f
            barBorderColor = Color.Transparent.toArgb()
        }

        totalTime = (if (empty) 4 else amounts.totalSleepMinutes) * 60

        when {
            empty -> {
                endTime = "-"
                startTime = "-"
            }

            else -> {
                endTime = amounts.lastOrNull()?.let {
                    DateTimeUtils.format(it.startDate, "HH:mm")
                } ?: "-"

                startTime = amounts.lastOrNull()?.let {
                    DateTimeUtils.format(it.endDate, "HH:mm")
                } ?: "-"
            }
        }
    }
}