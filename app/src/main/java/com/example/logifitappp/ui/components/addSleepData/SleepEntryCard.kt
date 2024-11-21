package com.example.logifitappp.ui.components.addSleepData

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.data.models.SleepEntry
import com.example.logifitappp.ui.components.Text
import java.time.LocalDateTime
import java.time.ZoneOffset

@Composable
 fun SleepEntryCard(
    entry: SleepEntry,
    onRemove: () -> Unit,
    onFellAsleepTimeSelected: (LocalDateTime) -> Unit,
    onWokeUpTimeSelected: (LocalDateTime) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.outline
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.sleep_period) + " "+ (entry.id + 1).toString(),
                    typography = MaterialTheme.typography.titleLarge,
                    fontWeight= FontWeight.Bold
                )
                IconButton(
                    onClick = onRemove,
                    enabled = entry.id != 0
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = if (entry.id != 0) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
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

            val diffMillis = entry.wokeUpTime.toInstant(ZoneOffset.UTC).toEpochMilli() -
                    entry.fellAsleepTime.toInstant(ZoneOffset.UTC).toEpochMilli()
            val hours = diffMillis / (1000 * 60 * 60)
            val minutes = (diffMillis / (1000 * 60)) % 60

            Text(
                text = stringResource(id = R.string.duration) + ": ${hours}h ${minutes}m",
                typography = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}