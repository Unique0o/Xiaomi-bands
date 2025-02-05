package com.example.logifitappp.ui.components.addSleepData


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.Text


@Composable
fun DurationSelector(
    title: String,
    selectedDuration: String?,
    onDurationSelected: (String) -> Unit
) {
    var showDurationPickerDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    typography = MaterialTheme.typography.titleMedium
                )

                selectedDuration?.let { duration ->
                    Text(
                        text = duration,
                        typography = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            IconButton(onClick = { showDurationPickerDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Select duration"
                )
            }
        }
    }

    if (showDurationPickerDialog) {
        DurationPicker(
            showDialog = showDurationPickerDialog,
            onDismiss = { showDurationPickerDialog = false },
            onDurationSelected = onDurationSelected
        )
    }
}