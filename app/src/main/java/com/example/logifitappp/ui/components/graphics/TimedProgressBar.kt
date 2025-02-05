package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.logifitappp.core.utils.DurationUtils
import com.example.logifitappp.ui.components.ProgressBar
import com.example.logifitappp.ui.components.Text

@Composable
fun TimedProgressBar(
    colors: List<Color>,
    duration: Long,
    endAt: String,
    progressValues: List<Float>,
    startAt: String
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.width(64.dp),
                text = DurationUtils.format(duration),
                typography = MaterialTheme.typography.bodySmall
            )

            ProgressBar(
                colors = colors,
                modifier = Modifier.fillMaxWidth(),
                progressValues = progressValues
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(Modifier.width(64.dp))

            Text(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = startAt,
                typography = MaterialTheme.typography.labelSmall
            )

            Spacer(Modifier.weight(1f))

            Text(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = endAt,
                typography = MaterialTheme.typography.labelSmall
            )
        }
    }
}