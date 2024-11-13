package com.example.logifitappp.core.graphics

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.logifitappp.core.analyzers.HeartRateAmountList
import com.example.logifitappp.ui.theme.Rose120
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class HeartRateDataSet(amounts: HeartRateAmountList) {
    var averageHeartRate: Float
        private set

    var empty = amounts.latestMeasuredHeartRate == 0L
        private set

    var self: BarDataSet
        private set

    var latestMeasuredHeartRate: Long
        private set

    var maxMeasuredHeartRate: Long
        private set

    var minMeasuredHeartRate: Long
        private set

    var yMax: Float
        private set

    init {
        averageHeartRate = amounts.averageHeartRate
        latestMeasuredHeartRate = amounts.latestMeasuredHeartRate
        maxMeasuredHeartRate = amounts.maxMeasuredHeartRate
        minMeasuredHeartRate = amounts.minMeasuredHeartRate

        yMax = if (empty) 10f else amounts.maxMeasuredHeartRate.toFloat()

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