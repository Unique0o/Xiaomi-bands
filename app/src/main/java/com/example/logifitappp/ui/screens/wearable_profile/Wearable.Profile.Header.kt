package com.example.logifitappp.ui.screens.wearable_profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.viewmodel.views.WearableProfileViewModel

@Composable
fun WearableProfileHeader(
    wearableProfileViewModel: WearableProfileViewModel
) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Chip(
            label = stringResource(if (wearableProfileViewModel.state.wearable?.isConnected() == true) R.string.connected else R.string.disconnected),
            status = if (wearableProfileViewModel.state.wearable?.isConnected() == true) ChipStatusEnum.SUCCESS else ChipStatusEnum.DANGER
        )

        Spacer(Modifier.height(8.dp))

        if (wearableProfileViewModel.state.wearable?.isConnected() == true) {
            wearableProfileViewModel.state.wearable?.getBatteryLevel().let {
                Spacer(Modifier.height(8.dp))

                IconText(
                    icon = Icons.Default.BatteryFull,
                    iconColor = MaterialTheme.colorScheme.surfaceTint,
                    label = stringResource(R.string.battery_percentage_label, "$it%"),
                    labelColor = MaterialTheme.colorScheme.surfaceTint,
                    labelTypography = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(8.dp))

            IconText(
                icon = Icons.Default.MonitorHeart,
                iconColor = MaterialTheme.colorScheme.surfaceTint,
                label = wearableProfileViewModel.state.totalSleepTimeMessage,
                labelColor = MaterialTheme.colorScheme.surfaceTint,
                labelTypography = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(8.dp))

        IconText(
            icon = Icons.Default.Sync,
            iconColor = MaterialTheme.colorScheme.surfaceTint,
            label = wearableProfileViewModel.state.activityDataSynchronizationMessage,
            labelColor = MaterialTheme.colorScheme.surfaceTint,
            labelTypography = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(8.dp))

        IconText(
            icon = Icons.Default.CloudSync,
            iconColor = MaterialTheme.colorScheme.surfaceTint,
            label = wearableProfileViewModel.state.synchronizationWithLogifitMessage,
            labelColor = MaterialTheme.colorScheme.surfaceTint,
            labelTypography = MaterialTheme.typography.bodyMedium
        )

        if (wearableProfileViewModel.state.wearable?.isConnected() != true) {
            Spacer(Modifier.height(8.dp))

            Text(
                color = MaterialTheme.colorScheme.surfaceTint,
                text = stringResource(R.string.wearable_profile_disconnecting_message),
                typography = MaterialTheme.typography.bodyMedium
            )
        }
    }
}