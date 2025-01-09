package com.example.logifitappp.core.graphics

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.logifitappp.core.analyzers.Spo2AmountList
import com.example.logifitappp.core.utils.ColorUtils
import com.example.logifitappp.ui.theme.Blue690
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class Spo2DataSet(amounts: Spo2AmountList) {
    val empty = amounts.latestMeasuredSpo2 == 0

    var self: BarDataSet
        private set

    val averageSpo2 = amounts.averageSpo2
    val latestMeasuredSpo2 = amounts.latestMeasuredSpo2
    val maxMeasuredSpo2 = amounts.maxMeasuredSpo2
    val minMeasuredSpo2 = amounts.minMeasuredSpo2

    val yMax = if (empty) 10f else amounts.maxMeasuredSpo2 + 10f
    val yMin = if (empty) 0f else amounts.minMeasuredSpo2 - 10f

    init {
        val entries = mutableListOf<BarEntry>()
        val gradients = mutableListOf<Int>()

        if (empty) {
            for (j in 0 .. 47) {
                entries.add(BarEntry(j.toFloat(), 10f))
                gradients.add(Blue690.copy(alpha = 0.15f).toArgb())
            }
        } else {
            amounts.getList().forEachIndexed { index, amount ->
                gradients.add(Color.Transparent.toArgb())

                when {
                    amount.modes.size == 0 -> gradients.add(ColorUtils.getGradientColor(intArrayOf(Color.Transparent.toArgb(), Color.Transparent.toArgb())))
                    amount.modes.size > 2 -> gradients.add(ColorUtils.getGradientColor(amount.modes.map { it.color }.toIntArray()))
                    else -> gradients.add(ColorUtils.getGradientColor(intArrayOf(amount.modes[0].color, amount.modes[0].color)))
                }

                entries.add(when {
                    amount.minSpo2 == amount.maxSpo2 && amount.maxSpo2 != 0 -> {
                        BarEntry(index.toFloat(), floatArrayOf(amount.minSpo2.toFloat(), 1.5f))
                    }
                    else -> BarEntry(index.toFloat(), floatArrayOf(amount.minSpo2.toFloat(), (amount.maxSpo2 - amount.minSpo2).toFloat()))
                })
            }
        }

        self = BarDataSet(entries, "SpO2 data set").apply {
            colors = gradients
        }
    }
}