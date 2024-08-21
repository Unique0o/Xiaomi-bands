package com.example.logifitappp.ui.components.graphics


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R
import com.example.logifitappp.ui.theme.*
import com.example.logifitappp.ui.components.graphics.HeaderIndicator
import com.example.logifitappp.ui.components.home.ConnectedIndicator

@Composable
fun InfoBarColorGraph(
    timeRange: String,
    data: List<Int>,

) {
    val barColors = List(17) {
        when (it % 4) {
            0 -> Blue690
            1 -> Violet
            2 -> Orange390
            else -> LightBlue
        }
    }.take(data.size)

    CardLayout(
        modifier = Modifier.padding(2.dp),
        icon = painterResource(id = R.drawable.ic_update),
        iconSize = 20.dp,
        label = stringResource(id = R.string.information_between),
        sutitle = timeRange,
        fontWeight = FontWeight.Medium,
        labelStyle = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Blue690
        ),

        suffixComponent = {
            ConnectedIndicator(
                text = "7h 36min",
                color = Green298,
                backgroundColor = Lime70,
                pointColor = Green298
            )
        },
        bodyComponent = {
            BarChart(data = data, colors = barColors)
            CardTimeLabels(
                hourStart = "22:00",
                hourFinal = "24:00",
                fontSize = 12.sp,
                textColor = Stone470

            )
        }
    )
}

@Composable
private fun BarChart(
    data: List<Int>,
    colors: List<Color>
) {
    val maxValue = data.maxOrNull() ?: 1
    val maxBarWidth = 1f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
    ) {
        data.forEachIndexed { index, value ->
            val barWidthFraction = maxBarWidth * (value.toFloat() / maxValue)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(barWidthFraction)
                    .background(colors.getOrElse(index) { LightBlue })
            )
        }
    }
}


