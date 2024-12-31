package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.logifitappp.core.graphics.SleepBarDataSet
import com.example.logifitappp.ui.components.Text
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarData

@Composable
fun SimpleHorizontalStackBarChart(
    modifier: Modifier =  Modifier.fillMaxWidth().height(40.dp),
    dataSet: SleepBarDataSet
) {
    Column {
        AndroidView(
            factory = { context ->
                HorizontalBarChart(context).apply {
                    setPinchZoom(false)
                    setTouchEnabled(false)
                    setFitBars(true)

                    description.isEnabled = false
                    legend.isEnabled = false
                    axisRight.isEnabled = false
                    minOffset = 0f

                    xAxis.apply {
                        isEnabled = false
                        setDrawLabels(false)
                        setDrawGridLines(false)
                        setDrawAxisLine(false)
                    }

                    axisLeft.apply {
                        isEnabled = false
                        axisMinimum = 0f
                        axisMaximum = dataSet.totalTime * 1f
                        setDrawLabels(false)
                        setDrawGridLines(false)
                        setDrawAxisLine(false)
                    }

                    data = BarData(dataSet.self).apply {
                        barWidth = 50.dp.value
                        setDrawValues(false)
                    }

                    invalidate()
                }
            },

            update = { chart ->
                chart.axisLeft.apply {
                    axisMaximum = dataSet.totalTime * 1f
                }

                chart.data = BarData(dataSet.self).apply {
                    barWidth = 50.dp.value
                    setDrawValues(false)
                }

                chart.invalidate()
            },

            modifier = modifier
        )

        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                color = MaterialTheme.colorScheme.outlineVariant,
                text = dataSet.startTime,
                typography = MaterialTheme.typography.labelSmall
            )

            Spacer(Modifier.weight(1f))

            Text(
                color = MaterialTheme.colorScheme.outlineVariant,
                text = dataSet.endTime,
                typography = MaterialTheme.typography.labelSmall
            )
        }
    }
}