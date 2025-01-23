package com.example.logifitappp.ui.screens.wearable_profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.viewmodel.views.WearableProfileViewModel

@Composable
fun WearableProfileOptions(
    navigation: NavHostController,
    user: UserModel?,
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

        Spacer(Modifier.height(12.dp))
    }

    if (user?.isAdmin() == true) {
        InformationCard(
            icon = Icons.Default.PieChart,
            label = stringResource(R.string.button_activity),
            labelColor = MaterialTheme.colorScheme.onSurface,
            labelTypography = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.clickable { wearableProfileViewModel.state.wearable?.getAddress()?.let { navigation.navigate(MainRoutes.Graphics(it)) } }
        )

        Spacer(Modifier.height(12.dp))
    }
}