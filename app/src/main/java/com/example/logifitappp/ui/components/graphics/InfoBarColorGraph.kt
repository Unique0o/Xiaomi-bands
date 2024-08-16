package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.home.ConnectedIndicator
import com.example.logifitappp.ui.theme.*

@Composable
fun InfoBarColorGraph(
    title: String,
    timeRange: String,
    data: List<Int>,
    textColor: Color = Blue690,
    accentColor: Color = Color(0xFF2196F3),
    modifier: Modifier = Modifier
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
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Header(title, timeRange, textColor, accentColor)
            Spacer(modifier = Modifier.height(16.dp))
            BarChart(data = data, colors = barColors)
            TimeLabels()
        }
    }
}

@Composable
private fun Header(
    title: String,
    timeRange: String,
    textColor: Color,
    accentColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh icon",
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$title $timeRange",
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        ConnectedIndicator(
            text = "7h 36min",
            color = Green298,
            backgroundColor = Lime70,
            pointColor = Green298,
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
    val maxBarWidth = 40.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        data.forEachIndexed { index, value ->
            val barWidth = (value.toFloat() / maxValue) * maxBarWidth
            Box(
                modifier = Modifier
                    .width(barWidth)
                    .fillMaxHeight()
                    .background(colors.getOrElse(index) { LightBlue })
            )
        }
    }
}

@Composable
fun DynamicBarGraphPreview() {
    val data = List(17) { (1..100).random() }
    InfoBarColorGraph(
        title = stringResource(id = R.string.information_between),
        timeRange = "19:00 - 07:00",
        data = data,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
@Preview(showBackground = true)
fun DynamicBarGraphPreview1() {
    DynamicBarGraphPreview()
}
