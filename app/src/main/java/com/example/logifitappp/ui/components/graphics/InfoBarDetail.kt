package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R
import com.example.logifitappp.data.models.HeartRateData
import com.example.logifitappp.ui.theme.*
import com.example.logifitappp.ui.components.graphics.HeaderIndicator

@Composable
fun InfoBarDetailGraph(
    data: List<Int>,
    time: String,
    hour: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.fillMaxWidth() .padding(16.dp)
        ) {
            HeaderRow(
                date = "Noviembre 20, 2023",
                title = stringResource(id = R.string.sleep_time),
            )



            HeartRateSummary(time, hour)
        }

        Spacer(modifier = Modifier.height(16.dp))
        val barColors = List(17) {
            when (it % 4) {
                0 -> Blue690
                1 -> Violet
                2 -> Orange390
                else -> LightBlue }
        }.take(data.size)

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BarChart(data = data, colors = barColors)

            Spacer(modifier = Modifier.height(8.dp))

            TimeLabels()
        }
    }
}

@Composable
fun HeartRateSummary(time: String, hour: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = time,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(id = R.string.information_between) + " " + hour,
            style = MaterialTheme.typography.labelSmall,
            color = Blue690
        )
    }
}

@Composable
private fun TimeLabels() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "22:00", fontSize = 12.sp, color = Stone470)
        Text(text = "06:00", fontSize = 12.sp, color = Stone470)
    }
}

@Composable
private fun BarChart(
    data: List<Int>,
    colors: List<Color>
) {
    val maxValue = data.maxOrNull() ?: 1
    val maxBarWidth = 1f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),

    ) {
        data.forEachIndexed { index, value ->
            val barWidthFraction = maxBarWidth * (value.toFloat() / maxValue)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(barWidthFraction)
                    .background(colors.getOrElse(index) { LightBlue })
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewInfoBarDetailGraph() {
    InfoBarDetailGraph(
        data = listOf(3, 5, 2, 4, 7, 6, 4),
        time = "7h 36min",
        hour = "19:00 - 07:00"
    )
}