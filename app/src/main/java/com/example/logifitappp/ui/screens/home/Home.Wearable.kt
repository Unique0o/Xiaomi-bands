package com.example.logifitappp.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.analyzers.ActivityAmount
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.cards.InformationOptionCard
import com.example.logifitappp.ui.components.graphics.bars.ProgressBar
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Slate450

@Composable
fun HomeWearable(
    connect: (wearable: Wearable) -> Unit,
    fetchActivities: (wearable: Wearable) -> Unit,
    sleeps: SnapshotStateList<ActivityAmount>,
    wearable: Wearable
) {

    if (!wearable.isConnected() && !wearable.isInitialized()) {
        InformationOptionCard(
            buttonIcon = Icons.Filled.Bluetooth,
            icon = Icons.Outlined.Watch,
            modifier = Modifier.padding(horizontal = 6.dp),
            onClick = { connect(wearable) },
            paragraph = stringResource(id = R.string.reminder_message),
            title = wearable.getAliasOrName()
        )
    } else if (wearable.isInitialized() && wearable.getWearableCoordinator().supportsActivityDataFetching()) {
        InformationOptionCard(
            buttonIcon = Icons.Filled.Sync,
            icon = Icons.Outlined.Watch,
            modifier = Modifier.padding(horizontal = 6.dp),
            onClick = { fetchActivities(wearable) },
            paragraph = stringResource(id = R.string.reminder_message),
            title = wearable.getAliasOrName()
        )
    }

    Column {
        sleeps.forEach {
            Column {
                Text(text = "time: ${it.getDurationTime()}")

                ProgressBar(
                    primaryProgressColor = if (it.isSleep()) Blue690 else Slate450,
                    primaryProgressFraction = 1f,
                    secondaryProgressFraction = 0f
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = DateTimeUtils.formatReducedIso8601(it.startDate),
                        typography = MaterialTheme.typography.labelSmall
                    )

                    Text(
                        modifier = Modifier.padding(end = 16.dp),
                        text = DateTimeUtils.formatReducedIso8601(it.endDate),
                        typography = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}