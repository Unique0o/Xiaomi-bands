package com.example.logifitappp.ui.components.graphics

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.home.ConnectedIndicator
import com.example.logifitappp.ui.theme.*

data class StepGraphicHomeData(
    val date: String,
    val minRate: Int,
    val maxRate: Int,
    val timeRange: String,
    val steps: List<Int>
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
                text = "1200 pasos",
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
fun StepHomeCard( steps: List<Int>,  modifier: Modifier = Modifier,) {

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
                title = "200kcal",
                accentColor = Blue690 ,
                backgroundColorConnect = Orange170,
                iconResId = R.drawable.ic_fire,
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

@Preview
@Composable
fun StepHomeCardPreview() {
    val steps = listOf(10, 20, 15, 30, 25, 35, 40)
    LogifitApppTheme(darkTheme = true) {
        StepHomeCard(steps)
    }
}