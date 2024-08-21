package com.example.logifitappp.ui.components.graphics


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.logifitappp.R
import com.example.logifitappp.ui.theme.*
import com.example.logifitappp.data.models.HeartRateData
import com.example.logifitappp.ui.components.home.ConnectedIndicator


@Composable
fun MeasurementHeartCard(
    heartRateData: HeartRateData,
    modifier: Modifier = Modifier,
    titleAccentColor: Color = Blue690,
    indicatorAccentColor: Color = Green298,
    indicatorBackgroundColor: Color = Lime70,
    indicatorInformation: String,
    icon : Painter
) {
    CardLayout(
        modifier = modifier,
        icon = icon,
        label = stringResource(id = R.string.graph_card_heart_rate),
        labelStyle = MaterialTheme.typography.titleMedium.copy(color = titleAccentColor),
        suffixComponent = {
            ConnectedIndicator(
                text = indicatorInformation,
                color = indicatorAccentColor,
                pointColor = indicatorAccentColor,
                backgroundColor = indicatorBackgroundColor
            )
        },
        bodyComponent = {
            Column {
                HeartRateCharts(heartRateData.ranges)
                CardTimeLabels(
                    hourStart = "00:00",
                    hourFinal = "24:00",
                    fontSize = 12.sp,
                    textColor = Stone470

                )
            }
        }
    )


}

@Composable
fun HeartRateCharts(ranges: List<Pair<Int, Int>>, modifier: Modifier = Modifier) {
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
