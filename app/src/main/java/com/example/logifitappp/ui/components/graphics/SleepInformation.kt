package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SleepTypeInfo(
    color: Color,
    type: String,
    percentage: String,
    duration: String,
    isColumn: Boolean
) {
    val content = @Composable {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "$type ($percentage)",
                style = MaterialTheme.typography.bodySmall,

                )
            Text(
                text = duration,
                style = MaterialTheme.typography.bodyMedium,

                fontWeight = FontWeight.Bold
            )
        }
    }

    if (isColumn) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                content()
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}