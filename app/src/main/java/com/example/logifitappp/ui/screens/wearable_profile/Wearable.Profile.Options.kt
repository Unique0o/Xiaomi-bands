package com.example.logifitappp.ui.screens.wearable_profile

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.viewmodel.views.WearableProfileViewModel

@Composable
fun WearableProfileOptions(
    wearableProfileViewModel: WearableProfileViewModel
) {
    if (wearableProfileViewModel.state.wearable?.isInitialized() == true) {
        InformationCard(
            icon = Icons.Default.Vibration,
            label = stringResource(R.string.button_find_my_band),
            labelColor = MaterialTheme.colorScheme.onSurface,
            labelTypography = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.clickable { wearableProfileViewModel.findSmartBand() }
        )
    }
}