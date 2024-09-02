package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.home.ConnectedIndicator
import com.example.logifitappp.ui.theme.*

@Composable
fun StepHomeCard(
    steps: List<Int>,
    modifier: Modifier = Modifier
) {
    CardLayout(
        icon = painterResource(id = R.drawable.ic_fire),
        iconSize = 20.dp,
        label = "200 kcal",
        labelStyle = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Blue690
        ),
        suffixComponent = {
            ConnectedIndicator(
                text = "1200 pasos",
                color = Green298,
                backgroundColor = Lime70,
                pointColor = Green298
            )
        },
        bodyComponent = {
            StepHomeChart(steps)
            CardTimeLabels(
                hourStart = "00:00",
                hourFinal = "24:00",
                fontSize = 12.sp,
                textColor = Stone470

            )
        }
    )
}

@Composable
private fun StepHomeChart(steps: List<Int>) {
    val barWidth = 6.dp
    val barSpacing = 16.dp
    val maxValue = 50

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
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
                            Green298,
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