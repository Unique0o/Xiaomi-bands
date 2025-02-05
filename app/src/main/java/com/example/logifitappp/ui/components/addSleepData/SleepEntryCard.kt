package com.example.logifitappp.ui.components.addSleepData

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.data.models.SleepEntry
import com.example.logifitappp.ui.components.Text
import java.time.LocalDateTime


@Composable
fun SleepEntryCard(
    entry: SleepEntry,
    index: Int,
    onFellAsleepTimeSelected: (LocalDateTime) -> Unit,
    onWokeUpTimeSelected: (LocalDateTime) -> Unit,
    onDurationSelected: (String) -> Unit,
    onRemove: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.sleep_period) + " ${index + 1}",
                    typography = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                if (onRemove != null) {
                    IconButton(onClick = onRemove) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }

            SleepTimeSelector(
                title = stringResource(id = R.string.when_fell_asleep),
                selectedDateTime = entry.fellAsleepTime,
                onDateTimeSelected = onFellAsleepTimeSelected
            )

            Spacer(modifier = Modifier.height(8.dp))

            SleepTimeSelector(
                title = stringResource(id = R.string.when_woke_up),
                selectedDateTime = entry.wokeUpTime,
                onDateTimeSelected = onWokeUpTimeSelected
            )

            Spacer(modifier = Modifier.height(8.dp))

            DurationSelector(
                title = stringResource(id = R.string.duration),
                selectedDuration = entry.duration,
                onDurationSelected = onDurationSelected
            )
        }
    }
}