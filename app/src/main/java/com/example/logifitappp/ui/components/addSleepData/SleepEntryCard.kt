package com.example.logifitappp.ui.components.addSleepData

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.data.models.SleepEntry
import java.time.LocalDateTime
import java.time.ZoneOffset

@Composable
fun SleepEntryCard(
    entry: SleepEntry,
    onFellAsleepTimeSelected: (LocalDateTime) -> Unit,
    onWokeUpTimeSelected: (LocalDateTime) -> Unit,
    onDurationSelected: (String) -> Unit
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