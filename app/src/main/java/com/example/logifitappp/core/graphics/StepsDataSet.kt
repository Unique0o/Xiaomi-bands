package com.example.logifitappp.core.graphics

import androidx.compose.ui.graphics.toArgb
import com.example.logifitappp.core.analyzers.StepsAmountList
import com.example.logifitappp.ui.theme.Green298
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class StepsDataSet(amounts: StepsAmountList) {
    val empty = amounts.totalSteps == 0L

    var self: BarDataSet
        private set

    val totalSteps = amounts.totalSteps

    val yMax = if (empty) 10f else amounts.maxStepsAmount.toFloat()

    init {
        val entries = mutableListOf<BarEntry>()

        if (empty) {
            for (j in 0 .. 47) entries.add(BarEntry(j.toFloat(), 10f))
        } else {
            amounts.getList().forEachIndexed { index, amount ->
                entries.add(BarEntry(index.toFloat(), amount.totalSteps.toFloat()))
            }
        }

        self = BarDataSet(entries, "Steps data set").apply {
            setColors(if (empty) Green298.copy(alpha = 0.15f).toArgb() else Green298.toArgb())
        }
    }
}