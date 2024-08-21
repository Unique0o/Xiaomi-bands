package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R
import com.example.logifitappp.ui.theme.*

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
fun StepHomeCard(
    steps: List<Int>,
    title: String,
    titleAccentColor: Color = Blue690,
    indicatorAccentColor: Color = Green298,
    indicatorBackgroundColor: Color = Lime70,
    indicatorInformation: String,
    icon : Painter
) {

    Card(
        modifier = Modifier.fillMaxWidth() .padding(2.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.outline
        )

    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            HeaderIndicator(
                title = title,
                subtitle = "kcal",
                titleAccentColor = titleAccentColor,
                indicatorAccentColor = indicatorAccentColor,
                indicatorBackgroundColor = indicatorBackgroundColor,
                textIndicator = indicatorInformation,
                icon = icon
            )
            StepHomeChart(steps)
            TimeLabels()
        }
    }
}

@Composable
fun StepHomeChart( steps: List<Int>, modifier: Modifier = Modifier) {
    val barWidth = 6.dp
    val barSpacing = 16.dp
    val maxValue = 50
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
            // Bars
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 24.dp, bottom = 1.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                steps.forEach { step ->
                    Box(
                        modifier = Modifier
                            .width(barWidth)
                            .fillMaxHeight(step.toFloat() / maxValue)
                            .background(
                                Color(0xFF8BC34A),
                                RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(barSpacing))
                }
            }
        }
    }

