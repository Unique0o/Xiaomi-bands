package com.example.logifitappp.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Battery4Bar
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.cards.AlertCard
import com.example.logifitappp.ui.components.cards.InformationOptionCard
import com.example.logifitappp.ui.components.cards.InformationOptionCardContent
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.ui.components.cards.SleepProcessingCard
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Orange390
import com.example.logifitappp.viewmodel.views.HomeViewModel

@Composable
fun HomeWearable(
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
        Row (
            modifier = Modifier.padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconText(
                icon = Icons.Outlined.Watch,
                iconColor = MaterialTheme.colorScheme.onSurface,
                iconSize = 30.dp,
                label = wearable.getAliasOrName(),
                labelTypography = MaterialTheme.typography.displayMedium,
                modifier = Modifier.clickable { navigation.navigate(MainRoutes.WearableProfile(wearable.getAddress()!!)) }
            )

            if (homeViewModel.state.drowsiness != null) {
                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                    horizontalPadding = 10.dp,
                    icon = Icons.Default.Share,
                    iconSize = 10.dp,
                    modifier = Modifier.height(24.dp),
                    text = stringResource(id = R.string.button_share),
                    onClick = { homeViewModel.tryToShareSleep() },
                    verticalPadding = 0.dp,
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        InformationCard(
            icon = Icons.Default.Battery4Bar,
            label = stringResource(id = R.string.battery_percentage_label, "${wearable.getBatteryLevel()}%"),
            suffixComponent = {
                Chip(
                    label = stringResource(id = R.string.connected),
                    status = ChipStatusEnum.SUCCESS
                )
            }
        ) {
            InformationOptionCardContent(
                buttonColor = when (homeViewModel.state.isSleepSynchronizationRequired) {
                    true -> MaterialTheme.colorScheme.primary
                    false -> when (homeViewModel.state.isSynchronizationWithLogifitRequired) {
                        true -> Orange390
                        false -> Green298
                    }
                },
                buttonIcon = Icons.Filled.Sync,
                onClick = { homeViewModel.reportSleep(wearable) },
                paragraph = stringResource(id = when (homeViewModel.state.drowsinessCondition?.name?.lowercase()) {
                    "apto" -> R.string.fit_to_drive_message
                    "no apto" -> R.string.unfit_to_drive_message
                    else -> R.string.state_to_drive_not_found_message
                })
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (!homeViewModel.state.isSleepSynchronizationRequired && homeViewModel.state.isSynchronizationWithLogifitRequired) {
                AlertCard(
                    message = stringResource(
                        when {
                            homeViewModel.state.isBandTheft -> R.string.band_theft_alert
                            (homeViewModel.state.drowsiness?.totalSleepSeconds ?: 0L) == 0L -> R.string.sync_without_sleep_data_alert
                            else -> R.string.sync_with_logifit_required_alert
                        }
                    ),
                    modifier = Modifier.clickable {
                        if (!homeViewModel.state.isBandTheft && (homeViewModel.state.drowsiness?.totalSleepSeconds ?: 0L) > 0L) {
                            homeViewModel.sendSleep(wearable)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            SleepProcessingCard(
                drowsiness = homeViewModel.state.drowsiness,
                drowsinessCondition = homeViewModel.state.drowsinessCondition,
                fatigue = homeViewModel.state.fatigue,
                tenant = homeViewModel.state.tenant
            )
        }
    }
}