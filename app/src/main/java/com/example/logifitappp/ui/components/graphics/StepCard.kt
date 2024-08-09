package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R
import com.example.logifitappp.ui.screens.graphics.MainScreen1
import com.example.logifitappp.ui.theme.LogifitApppTheme


@Composable
fun StepChart(
    steps: List<Int>,
    maxValue: Int,
    modifier: Modifier = Modifier
) {
    val barWidth = 6.dp
    val barSpacing = 16.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(top = 16.dp, bottom = 24.dp, end = 16.dp)
    ) {
        ChartGrids()
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "50", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(text = "25", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(text = "0", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }

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

        HorizontalDivider(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(end = 2.dp),
            thickness = 0.5.dp,
            color = Color.LightGray
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth()
                .padding(end = 24.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("00:00", "04:00", "08:00", "12:00", "16:00").forEach { time ->
                Text(
                    text = time,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.Center,
                )

            }
        }
    }
}
@Composable
fun ChartGrids() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val yStep = size.height / 3
        val xStep = size.width / 5

        // Vertical lines
        for (i in 0..5) {
            drawLine(
                color = Color.LightGray.copy(alpha = 0.3f),
                start = Offset(i * xStep, 0f),
                end = Offset(i * xStep, size.height),
                strokeWidth = 0.5f
            )
        }

        // Horizontal lines
        for (i in 0..3) {
            drawLine(
                color = Color.LightGray.copy(alpha = 0.3f),
                start = Offset(0f, i * yStep),
                end = Offset(size.width, i * yStep),
                strokeWidth = 0.5f
            )
        }
    }
}

@Composable
fun StepTrackingCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Previous day",
                    tint = Color.Gray
                )
                Text(
                    text = "Noviembre 20, 2023",
                    style = MaterialTheme.typography.labelMedium
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_forward),
                    contentDescription = "Next day",
                    tint = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pasos realizados",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    text = "1200 pasos",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF3F51B5),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                StepChart(
                    steps = listOf(10, 20, 15, 30, 25, 35, 40),
                    maxValue = 50
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GraphicsPreview1() {
    LogifitApppTheme {
        StepTrackingCard()
    }
}
