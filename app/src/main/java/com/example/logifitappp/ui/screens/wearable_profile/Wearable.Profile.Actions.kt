package com.example.logifitappp.ui.screens.wearable_profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.viewmodel.views.WearableProfileViewModel

@Composable
fun WearableProfileActions(
    wearableProfileViewModel: WearableProfileViewModel
) {
    val wearable = wearableProfileViewModel.state.wearable ?: return

    Column(Modifier.fillMaxWidth()) {
        if (wearable.isInitialized() && wearable.getWearableCoordinator().supportsActivityDataFetching()) {
            IconButton(
                icon = Icons.Filled.Sync,
                modifier = Modifier.fillMaxWidth(),
                onClick = { wearableProfileViewModel.fetchActivities(wearable)  },
                text = stringResource(id = R.string.button_sync_sleep),
            )
        } else {
            IconButton(
                icon = Icons.Filled.Bluetooth,
                modifier = Modifier.fillMaxWidth(),
                onClick = { wearableProfileViewModel.connect(wearable) },
                text = stringResource(id = R.string.button_connect_smart_band),
            )
        }

        Spacer(Modifier.height(12.dp))

        IconButton(
            backgroundColor = MaterialTheme.colorScheme.surface,
            icon = Icons.Default.CloudSync,
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)), RoundedCornerShape(size = 24.dp)),
            onClick = { wearableProfileViewModel.sendSleep(wearable) },
            text = stringResource(id = R.string.button_send_to_logifit),
            textColor = MaterialTheme.colorScheme.primary
        )
    }
}