package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.ui.components.home.ConnectedIndicator

@Composable
fun HeaderIndicator(
    title: String,
    subtitle: String,
    titleAccentColor: Color,
    indicatorAccentColor: Color,
    indicatorBackgroundColor: Color,
    textIndicator: String,
    icon: Painter
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = icon,
                contentDescription = "Icon",
                tint = titleAccentColor,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = "$title $subtitle",
                color = titleAccentColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(8.dp))
            ConnectedIndicator(
                text = textIndicator,
                color = indicatorAccentColor,
                backgroundColor = indicatorBackgroundColor,
                pointColor = indicatorAccentColor
            )
        }
    }
}
