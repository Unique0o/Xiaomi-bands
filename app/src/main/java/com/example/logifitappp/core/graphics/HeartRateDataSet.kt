package com.example.logifitappp.core.graphics

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.logifitappp.core.analyzers.HeartRateAmountList
import com.example.logifitappp.ui.theme.Rose120
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class HeartRateDataSet(amounts: HeartRateAmountList) {
    val averageHeartRate = amounts.averageHeartRate
    val latestMeasuredHeartRate = amounts.latestMeasuredHeartRate

    val empty = amounts.latestMeasuredHeartRate == 0L

    var self: BarDataSet
        private set

    val maxMeasuredHeartRate = amounts.maxMeasuredHeartRate
    val minMeasuredHeartRate = amounts.minMeasuredHeartRate

    val yMax = if (empty) 10f else amounts.maxMeasuredHeartRate.toFloat()

    init {
        val entries = mutableListOf<BarEntry>()

        if (empty) {
            for (j in 0 .. 47) entries.add(BarEntry(j.toFloat(), 10f))
        } else {
            amounts.getList().forEachIndexed { index, amount ->
                entries.add(when {
                    amount.minHeartRate == amount.maxHeartRate && amount.maxHeartRate != 0L -> {
                        BarEntry(index.toFloat(), floatArrayOf(amount.minHeartRate.toFloat(), 5f))
                    }
                    else -> BarEntry(index.toFloat(), floatArrayOf(amount.minHeartRate.toFloat(), (amount.maxHeartRate - amount.minHeartRate).toFloat()))
                })
            }
        }

        self = BarDataSet(entries, "Steps data set").apply {
            if (empty) setColors(Rose120.copy(alpha = 0.15f).toArgb())
            else setColors(Color.Transparent.toArgb(), Rose120.toArgb())
        }
    }
}