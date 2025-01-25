package com.example.logifitappp.core.graphics

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.logifitappp.core.analyzers.StressAmountList
import com.example.logifitappp.ui.theme.Esmerald300
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class StressDataSet(amounts: StressAmountList) {
    val empty = amounts.latestMeasuredStress == 0

    var self: BarDataSet
        private set

    val averageStress = amounts.averageStress
    val latestMeasuredStress = amounts.latestMeasuredStress
    val maxMeasuredStress = amounts.maxMeasuredStress
    val minMeasuredStress = amounts.minMeasuredStress

    val yMax = if (empty) 10f else amounts.maxMeasuredStress + 10f
    val yMin = if (empty) 0f else amounts.minMeasuredStress - 10f

    init {
        val entries = mutableListOf<BarEntry>()

        if (empty) {
            for (j in 0 .. 47) entries.add(BarEntry(j.toFloat(), 10f))
        } else {
            amounts.getList().forEachIndexed { index, amount ->
                entries.add(when {
                    amount.minStress == amount.maxStress && amount.maxStress != 0 -> {
                        BarEntry(index.toFloat(), floatArrayOf(amount.minStress.toFloat(), 1.5f))
                    }
                    else -> BarEntry(index.toFloat(), floatArrayOf(amount.minStress.toFloat(), (amount.maxStress - amount.minStress).toFloat()))
                })
            }
        }

        self = BarDataSet(entries, "SpO2 data set").apply {
            if (empty) setColors(Esmerald300.copy(alpha = 0.15f).toArgb())
            else setColors(Color.Transparent.toArgb(), Esmerald300.toArgb())
        }
    }
}