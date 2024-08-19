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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.theme.*
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun ChartEmptyHour(
    steps: List<Int>,
    maxValue: Int,
    barColor: Color,
    accentColor:Color,
    modifier: Modifier = Modifier
) {
    val chartHeight = 130.dp
    val columnWidth = 20.dp
    val timeLabels = listOf("00:00", "04:00", "08:00", "12:00", "16:00")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(chartHeight)
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        // Chart area
        Box(
            modifier = Modifier
                .weight(1f)
                .height(chartHeight)
                .drawBehind {
                    drawRect(
                        color = Stone470,
                        size = Size(1.dp.toPx(), size.height),
                        topLeft = Offset(size.width - 1.dp.toPx(), 0f)
                    )
                }
        ) {
            EmptyGraph(
                data = steps.map { it.toFloat() },
                barColor = barColor,
                accentColor = accentColor
            )
            Divider(
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
                .width(columnWidth)
                .fillMaxHeight()
                .padding(end = 4.dp, top= 1.dp,start= 2.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "$maxValue", style = MaterialTheme.typography.labelSmall, color = Stone470)
            Text(text = "${maxValue / 2}", style = MaterialTheme.typography.labelSmall, color = Stone470)
            Text(text = "0", style = MaterialTheme.typography.labelSmall, color = Stone470)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 40.dp, top= 1.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        timeLabels.forEach { time ->
            Text(
                text = time,
                style = MaterialTheme.typography.labelSmall,
                color = Stone470,
                modifier = Modifier.width(50.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun EmptyGraph(
    data: List<Float>,
    barColor: Color,
    accentColor: Color,
) {
    val maxValue = data.maxOrNull() ?: 1f
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { value ->
            Bar(
                height = value / maxValue,
                barColor = barColor,
                accentColor = accentColor,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun Bar(
    height: Float,
    barColor: Color,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = 1.dp)

    ) {
        // Barra de fondo (vacía)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(barColor)
        )
        // Barra de progreso
        //Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(height)
//                .align(Alignment.BottomCenter)
//                .background(accentColor)
//        )
    }
}

@Composable
fun EmptyCardHour(
    steps: List<Int>,
    maxValue: Int,
    barColor: Color,
    accentColor: Color,
    title: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(14.dp)
            .height(250.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors =CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val date = "Noviembre 20, 2023"
            HeaderRow(date, title)
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ChartEmptyHour(
                    steps = steps,
                    maxValue = maxValue,
                    barColor = barColor,
                    accentColor = accentColor,
                    modifier = Modifier
                        .fillMaxWidth()

                )
            }
        }
    }
}


@Composable
fun ChartEmptyHourExample() {
    val data = List(24) { (1..50).random() }

    EmptyCardHour(
        steps = data,
        maxValue = 50,
        barColor = Lime70,
        accentColor = Green298,
        title = "Pasos realizados",
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun ChartEmptyHourPreview() {
    LogifitApppTheme {
        ChartEmptyHourExample()
    }
}
