package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.logifitappp.ui.components.Text
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet

@Composable
fun VerticalBarChart(
    modifier: Modifier = Modifier,
    dataSet: BarDataSet,
    yMax: Float,
    yMin: Float = 0f
) {
    Column {
        AndroidView(
            factory = { context ->
                BarChart(context).apply {
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
                        axisMinimum = yMin
                        axisMaximum = yMax
                        setDrawLabels(false)
                        setDrawGridLines(false)
                        setDrawAxisLine(false)
                    }

                    data = BarData(dataSet).apply {
                        setDrawValues(false)
                        barWidth = 0.5f
                    }

                    invalidate()
                }
            },

            update = { chart ->
                chart.data = BarData(dataSet).apply {
                    setDrawValues(false)
                    barWidth = 0.5f
                }

                chart.axisLeft.apply {
                    axisMaximum = yMax
                    axisMinimum = yMin
                }

                chart.invalidate()
            },

            modifier = modifier
        )

        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                color = MaterialTheme.colorScheme.outlineVariant,
                text = "00:00",
                typography = MaterialTheme.typography.labelSmall
            )

            Spacer(Modifier.weight(1f))

            Text(
                color = MaterialTheme.colorScheme.outlineVariant,
                text = "24:00",
                typography = MaterialTheme.typography.labelSmall
            )
        }
    }
}