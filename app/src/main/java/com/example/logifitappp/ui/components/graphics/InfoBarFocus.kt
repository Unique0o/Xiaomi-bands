package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.theme.*

@Composable
fun InfoBarFocusGraph(
    data: List<Int>,
    time: String,
    hour: String,
    specialBarIndex: Int? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            HeaderRow(
                date = "Noviembre 20, 2023",
                title = stringResource(id = R.string.sleep_time),
                firstAlternativeTitle = time,
                firstAlternativeSubtitle = stringResource(id = R.string.information_between),
                secondAlternativeSubtitle = hour
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        val barColors = List(17) {
            when (it % 4) {
                0 -> Gray615
                1 -> Slate450
                2 -> Stone510
                else -> Slate520
            }
        }.take(data.size)

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BarChart(data = data, colors = barColors, specialBarIndex = specialBarIndex)
            Spacer(modifier = Modifier.height(8.dp))
            CardTimeLabels(hourStart = "22:00" , hourFinal = "06:00")
        }
    }
}

@Composable
private fun BarChart(
    data: List<Int>,
    colors: List<Color>,
    specialBarIndex: Int? = null
) {
    val maxValue = data.maxOrNull() ?: 1
    val maxBarWidth = 1f
    val specialColor = Sky320

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        data.forEachIndexed { index, value ->
            val barWidthFraction = maxBarWidth * (value.toFloat() / maxValue)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(barWidthFraction)
                    .background(
                        if (index == specialBarIndex) specialColor
                        else colors.getOrElse(index) { Sky320 }
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInfoBarFocusGraph() {
    InfoBarFocusGraph(
        data = listOf(3, 5, 2, 4, 7, 6, 4),
        time = "7h 36min",
        hour = "19:00 - 07:00",
        specialBarIndex = 0
    )
}
