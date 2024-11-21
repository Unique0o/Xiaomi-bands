package com.example.logifitappp.ui.components.addSleepData

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.time.TimePicker
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun SleepTimeSelector(
    title: String,
    selectedDateTime: LocalDateTime?,
    onDateTimeSelected: (LocalDateTime) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.outline
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    typography = MaterialTheme.typography.titleMedium
                )

                selectedDateTime?.let { dateTime ->
                    Text(
                        text = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        typography = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            TimePicker(
                onDateTimeSelected = onDateTimeSelected
            )

        }
    }
}