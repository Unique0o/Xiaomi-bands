package com.example.logifitappp.ui.components.graphics

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.logifitappp.R
import com.example.logifitappp.ui.theme.*
import com.example.logifitappp.data.models.HeartRateData

@Composable
fun HeartRateCard(heartRateData: HeartRateData) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        DateSelector(heartRateData.date)
        Spacer(modifier = Modifier.height(16.dp))
        HeartRateSummary(heartRateData)
        Spacer(modifier = Modifier.height(24.dp))
        HeartRateChart(heartRateData.ranges)
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
        Text(
            text = date,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
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
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = data.timeRange,
            style = MaterialTheme.typography.labelSmall,
            color = Stone470
        )
    }
}

@Composable
fun HeartRateChart(ranges: List<Pair<Int, Int>>, modifier: Modifier = Modifier) {
    val maxRate = ranges.maxOfOrNull { it.second } ?: 0
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        // Chart area
        Box(
            modifier = Modifier
                .weight(1f)
                .height(100.dp)

                .drawBehind {
                    drawRect(
                        color = Stone470,
                        size = Size(1.dp.toPx(), size.height),
                        topLeft = Offset(size.width - 1.dp.toPx(), 0f)
                    )
                }
        ) {
            ChartGrids()
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.Bottom
            ) {
                ranges.forEach { range ->
                    val heightFraction = (range.second - range.first).toFloat() / maxRate
                    val startFraction = range.first.toFloat() / maxRate

                    Box(
                        modifier = Modifier
                            .fillMaxHeight(heightFraction)
                            .padding(horizontal = 6.dp)
                            .width(5.dp)
                            .offset(y = -(startFraction * 80.dp))
                            .clip(RoundedCornerShape(2.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Rose120)
                        )
                    }
                }
            }
            HorizontalDivider(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(end = 2.dp),
                thickness = 0.5.dp,
                color = Stone470
            )
        }

        Column(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight()
                .padding(end = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "50", style = MaterialTheme.typography.labelSmall, color = Stone470)
            Text(text = "25", style = MaterialTheme.typography.labelSmall, color = Stone470)
            Text(text = "0", style = MaterialTheme.typography.labelSmall, color = Stone470)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 40.dp, top = 1.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        listOf("00:00", "04:00", "08:00", "12:00", "16:00").forEach { time ->
            Text(
                text = time,
                style = MaterialTheme.typography.labelSmall,
                color = Stone470,
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}



@Composable
@Preview(showBackground = true)
fun HeartRateCardPreview1() {
    val heartdata = HeartRateData(
        date = "Noviembre 20, 2023",
        minRate = 70,
        maxRate = 101,
        timeRange = "02:00 - 02:30",
        ranges = listOf(
            10 to 35,
            25 to 45,
            15 to 40,
            20 to 40,
            10 to 40,
        )
    )
    HeartRateCard(heartdata)
}