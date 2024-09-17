package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.graphics.bars.EmptyBars
import com.example.logifitappp.ui.theme.Blue690

@Composable
fun EmptyInfoGraph(
    title: String,
    timeRange: String,
    titleAccentColor: Color = Blue690,
    indicatorAccentColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    indicatorBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    indicatorInformation: String,
    icon: Painter,
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            HeaderIndicator(
                title = title,
                subtitle = timeRange,
                titleAccentColor = titleAccentColor,
                indicatorAccentColor = indicatorAccentColor,
                indicatorBackgroundColor = indicatorBackgroundColor,
                textIndicator = indicatorInformation,
                icon = icon
            )
            Spacer(modifier = Modifier.height(16.dp))
            EmptyBars()
        }
    }
}



