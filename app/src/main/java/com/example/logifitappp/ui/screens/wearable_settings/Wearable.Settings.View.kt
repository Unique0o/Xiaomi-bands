package com.example.logifitappp.ui.screens.wearable_settings

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.cards.SettingCard
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.modals.UnpairWearableModal
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.views.WearableSettingsViewModel

@Composable
fun WearableSettingsView(
    navigation: NavHostController,
    mac: String
) {
    val wearableSettingsViewModel = hiltViewModel<WearableSettingsViewModel, WearableSettingsViewModel.WearableSettingsViewModelFactory> {
        it.create(navigation, mac)
    }

    UnpairWearableModal(
        onClose = { wearableSettingsViewModel.closeUnpairWearableModal() },
        onDismissRequest = { wearableSettingsViewModel.closeUnpairWearableModal() },
        onUnpairedWearable = { wearableSettingsViewModel.handleUnpairWearable(it) },
        visible = wearableSettingsViewModel.state.shouldShowUnpairWearableModal,
        wearable = wearableSettingsViewModel.state.wearable
    )

    ScrollablePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.wearable_settings_title)
            )
        }
    ) {
        item {
            SettingCard(
                icon = Icons.Default.LinkOff,
                modifier = Modifier.clickable { wearableSettingsViewModel.openUnpairWearableModal() },
                summary = stringResource(id = R.string.unpair_wearable_setting_summary),
                title = stringResource(id = R.string.unpair_wearable_setting_title)
            )
        }
    }
}