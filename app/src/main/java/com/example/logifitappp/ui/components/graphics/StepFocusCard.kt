package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.ui.theme.Stone470

@Composable
fun StepChart(
    steps: List<Int>,
    highlightIndex: Int,
    modifier: Modifier = Modifier
) {
    val barWidth = 6.dp
    val barSpacing = 16.dp
    val maxValue = 50
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        // Chart area
        Box(
            modifier = Modifier
                .weight(1f)
                .height(200.dp)
                .drawBehind {
                    drawRect(
                        color = Stone470,
                        size = Size(1.dp.toPx(), size.height),
                        topLeft = Offset(size.width - 1.dp.toPx(), 0f)
                    )
                }
        ) {
            ChartGrids()
            // Bars
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 24.dp, bottom = 1.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                steps.forEachIndexed { index, step ->
                    val barColor = if (index == highlightIndex) Green298 else Lime70

                    Box(
                        modifier = Modifier
                            .width(barWidth)
                            .fillMaxHeight(step.toFloat() / maxValue)
                            .background(
                                barColor,
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
                color = Stone470
            )
        }

        Column(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight()
                .padding(end = 15.dp),
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
fun StepCard(modifier: Modifier = Modifier, steps: List<Int>, highlightIndex: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(320.dp)
    ) {
        HeaderRow(date = "Noviembre 20, 2023", title = stringResource(id = R.string.steps_taken))
        Column(
            modifier = Modifier
                .height(280.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "1200 pasos",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            StepChart(
                steps = steps,
                highlightIndex = highlightIndex
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StepFocusPreview() {
    val steps = listOf(10, 20, 15, 30, 25, 35, 40)
    LogifitApppTheme {
        StepCard(
            steps = steps,
            highlightIndex = 1
        )
    }
}
