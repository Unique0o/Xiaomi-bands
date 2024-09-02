package com.example.logifitappp.ui.components.graphics

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.home.ConnectedIndicator
import com.example.logifitappp.ui.theme.*

data class MeasurementHeartData(
    val date: String,
    val minRate: Int,
    val maxRate: Int,
    val timeRange: String,
    val ranges: List<Pair<Int, Int>>
)
@Composable
private fun Header(
    title: String,
    accentColor: Color,
    backgroundColorConnect: Color,
    @DrawableRes iconResId: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = "Calories icon",
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = title,
                color = accentColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {

            Spacer(modifier = Modifier.width(8.dp))
            ConnectedIndicator(
                text = "90 LPM",
                color =  Green298,
                backgroundColor =  Lime70,
                pointColor =  Green298
            )
        }
    }
}
@Composable
private fun TimeLabels() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "00:00", fontSize = 12.sp, color = Stone470)
        Text(text = "24:00", fontSize = 12.sp, color = Stone470)
    }
}

@Composable
fun MeasurementHeartCard(heartRateData1: MeasurementHeartData,  modifier: Modifier = Modifier,) {

    Card(
        modifier = modifier.fillMaxWidth() .padding(15.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.outline
        )

    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Header(
                title = stringResource(id = R.string.graph_card_heart_rate),
                accentColor = Blue690 ,
                backgroundColorConnect = Orange170,
                iconResId = R.drawable.ic_heart_cog,
            )
            HeartRateChart1(heartRateData1.ranges)
            TimeLabels()
        }
    }
}

@Composable
fun HeartRateChart1(ranges: List<Pair<Int, Int>>, modifier: Modifier = Modifier) {
    val maxRate = ranges.maxOfOrNull { it.second } ?: 0
    Row(
        modifier = modifier

            .height(80.dp)
            .padding(top = 11.dp, bottom = 8.dp)
    ) {
        // Chart area
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
                        .offset(y = -(startFraction * 50.dp))
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
    }
}
