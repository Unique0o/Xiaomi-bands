package com.example.logifitappp.ui.screens.synchronization_report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.ui.components.Text

@Composable
fun SynchronizationReportResumeItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            color = valueColor,
            modifier = Modifier.fillMaxWidth(),
            text = value,
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp, lineHeight = 28.sp)
        )

        Spacer(Modifier.height(4.dp))

        Text(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            text = label,
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.labelSmall
        )
    }
}