package com.example.logifitappp.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.cards.InformationOptionCard
import com.example.logifitappp.viewmodel.views.HomeViewModel

@Composable
fun  HomeWearable(
    homeViewModel: HomeViewModel,
    navigation: NavHostController,
) {
    val wearable = homeViewModel.wearables.firstOrNull()

    if (wearable == null) {
        InformationOptionCard(
            buttonIcon = Icons.Filled.Add,
            icon = Icons.Outlined.Watch,
            modifier = Modifier.padding(horizontal = 6.dp),
            onClick = { navigation.navigate(MainRoutes.WearableDetection) },
            paragraph = stringResource(id = R.string.reminder_message),
            title =  stringResource(id = R.string.my_device)
        )

        return
    }

    if (!wearable.isConnected() && !wearable.isInitialized()) {
        InformationOptionCard(
            buttonIcon = Icons.Filled.Bluetooth,
            icon = Icons.Outlined.Watch,
            modifier = Modifier.padding(horizontal = 6.dp),
            onClick = { homeViewModel.connect(wearable) },
            paragraph = stringResource(id = R.string.reminder_message),
            title = wearable.getAliasOrName()
        )
    } else if (wearable.isInitialized() && wearable.getWearableCoordinator().supportsActivityDataFetching()) {
        InformationOptionCard(
            buttonIcon = Icons.Filled.Sync,
            icon = Icons.Outlined.Watch,
            modifier = Modifier.padding(horizontal = 6.dp),
            onClick = { homeViewModel.fetchActivities(wearable) },
            paragraph = stringResource(id = R.string.reminder_message),
            title = wearable.getAliasOrName()
        )
    }
}