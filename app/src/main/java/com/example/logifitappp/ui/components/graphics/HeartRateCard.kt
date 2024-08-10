package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R

data class HeartRateData(
    val date: String,
    val minRate: Int,
    val maxRate: Int,
    val timeRange: String,
    val rates: List<Int>
)

@Composable
fun HeartRateCard(heartRateData: HeartRateData, modifier: Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
       colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            DateSelector(heartRateData.date)
            Spacer(modifier = Modifier.height(16.dp))
            HeartRateSummary(heartRateData)
            Spacer(modifier = Modifier.height(24.dp))
            HeartRateChart(heartRateData.rates)
        }
    }
}

@Composable
fun DateSelector(date: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* TODO */ }) {
            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Previous day")
        }
        Text(text = date, style = MaterialTheme.typography.labelMedium)
        IconButton(onClick = { /* TODO */ }) {
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next day")
        }
    }
}

@Composable
fun HeartRateSummary(data: HeartRateData) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.heart_rate_card_title),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Min: ${data.minRate} LPM - Max: ${data.maxRate} LPM",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Blue
        )
        Text(
            text = data.timeRange,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}

@Composable
fun HeartRateChart(rates: List<Int>) {
    val maxRate = rates.maxOrNull() ?: 0
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(top = 16.dp, bottom = 24.dp, end = 16.dp)
            .drawBehind {
                drawRect(
                    color = Color.LightGray,
                    size = Size(1.dp.toPx(), size.height),
                    topLeft = Offset(size.width - 1.dp.toPx(), 0f)
                )
            }
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            val yStep = size.height / 3
            for (i in 0..2) {
                drawLine(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    start = Offset(0f, i * yStep),
                    end = Offset(size.width, i * yStep),
                    strokeWidth = 0.5f
                )
            }

            drawLine(
                color = Color.LightGray,
                start = Offset(0f, 0f),
                end = Offset(0f, size.height),
                strokeWidth = 1f
            )

            drawLine(
                color = Color.LightGray,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = 1f
            )
        }

        // Y-axis labels
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),

            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text("50", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Text("25", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Text("0", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }

        // Bars
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            rates.forEach { rate ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(rate.toFloat() / maxRate)
                        .padding(horizontal = 20.dp)
                        .width(4.dp)
                        .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red.copy(alpha = 0.3f))
                    )
                }
            }
        }

        // X-axis labels
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("00:00", "04:00", "08:00", "12:00", "16:00", "20:00").forEach { time ->
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun HeartRateCardPreview() {
    val sampleData = HeartRateData(
        date = "Noviembre 20, 2023",
        minRate = 70,
        maxRate = 101,
        timeRange = "02:00 - 02:30",
        rates = listOf(30, 45, 20, 35, 25, 40)
    )
    HeartRateCard(sampleData, modifier = Modifier.fillMaxWidth())
}