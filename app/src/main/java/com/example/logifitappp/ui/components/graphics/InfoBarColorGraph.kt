package com.example.logifitappp.ui.components.graphics


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.ui.theme.*
import com.example.logifitappp.ui.components.graphics.HeaderIndicator

@Composable
fun InfoBarColorGraph(
    title: String,
    timeRange: String,
    data: List<Int>,
    titleAccentColor: Color = Blue690,
    indicatorAccentColor: Color = Green298,
    indicatorBackgroundColor: Color = Lime70,
    indicatorInformation: String,
    icon: Painter
) {
    val barColors = List(17) {
        when (it % 4) {
            0 -> Blue690
            1 -> Violet
            2 -> Orange390
            else -> LightBlue
        }
    }.take(data.size)

    Card(
        modifier = Modifier .padding(2.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.outline
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            HeaderIndicator(
                title = title,
                subtitle = timeRange,
                titleAccentColor = titleAccentColor,
                indicatorAccentColor = indicatorAccentColor,
                indicatorBackgroundColor = indicatorBackgroundColor,
                textIndicator = indicatorInformation,
                icon = icon
            )
            Spacer(modifier = Modifier.height(16.dp))
            BarChart(data = data, colors = barColors)
            TimeLabels()
        }
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


