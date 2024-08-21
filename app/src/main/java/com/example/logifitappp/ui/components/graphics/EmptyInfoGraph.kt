package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.home.ConnectedIndicator
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Lime30
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.ui.theme.Stone240
import com.example.logifitappp.ui.theme.Stone470
import com.example.logifitappp.ui.theme.Zinc680

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

@Composable
private fun EmptyBars() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 2.dp)
                    .background(Stone470)
            )
        }
    }
}


