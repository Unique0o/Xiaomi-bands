package com.example.logifitappp.ui.screens.synchronization_report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.logifitappp.ui.components.Text

@Composable
fun SynchronizationReportResumeItem(
    label: String,
    value: String
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            typography = MaterialTheme.typography.titleMedium
        )

        Text(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            text = value,
            typography = MaterialTheme.typography.labelMedium
        )
    }
}