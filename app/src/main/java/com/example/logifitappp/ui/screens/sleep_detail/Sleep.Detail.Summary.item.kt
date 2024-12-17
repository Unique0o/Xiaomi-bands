package com.example.logifitappp.ui.screens.sleep_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.Text
import java.util.Locale

@Composable
fun SleepDetailSummaryItem(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    percentage: Float,
    summary: String,
    title: String
) {
    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                contentDescription = "summary dot",
                imageVector = Icons.Default.Circle,
                modifier = Modifier.size(12.dp),
                tint = backgroundColor
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = "$title ",
                typography = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text ="(${String.format(Locale.ROOT, "%.0f", percentage)}%)",
                typography = MaterialTheme.typography.bodySmall,
            )
        }

        Row {
            Spacer(Modifier.width(18.dp))

            Text(
                text = summary,
                typography = MaterialTheme.typography.titleMedium
            )
        }
    }
}