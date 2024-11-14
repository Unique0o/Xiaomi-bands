package com.example.logifitappp.ui.components.graphics


import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.ui.components.graphics.bars.Bar
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Stone470

@Composable
fun StepEmptyGraphCard(
    title: String,
    data: List<Float>,
    icon: Painter,
    titleAccentColor: Color = Blue690,
    indicatorAccentColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    indicatorBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    barColor: Color,
    accentColor: Color = Green298,
    modifier: Modifier = Modifier,
    indicatorInformation: String
) {
    CardLayout(
        modifier = modifier,
        icon = icon,
        label = title,
        labelStyle = MaterialTheme.typography.titleMedium.copy(color = titleAccentColor),
        suffixComponent = {

        },
        bodyComponent = {
            Column {
                StepGraph(data, barColor, accentColor)
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
private fun StepGraph(
    data: List<Float>,
    barColor: Color,
    accentColor: Color
) {
    val maxValue = data.maxOrNull() ?: 1f
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { value ->
            Bar(
                height = (value / maxValue),
                barColor = barColor,
                accentColor = accentColor,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

