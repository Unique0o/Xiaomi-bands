package com.example.logifitappp.ui.screens.wearable_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.modals.MessageModal
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.WearableProfileViewModel

@Composable
fun WearableProfileView(
    appViewModel: AppViewModel,
    navigation: NavHostController,
    mac: String
) {
    val wearableProfileViewModel = hiltViewModel<WearableProfileViewModel, WearableProfileViewModel.WearableProfileViewModelFactory>{
        it.create(mac, appViewModel.user)
    }

    MessageModal(
        onClose = { wearableProfileViewModel.stopProcessing() },
        onDismissRequest = { wearableProfileViewModel.stopProcessing() },
        status = wearableProfileViewModel.state.status,
        visible = wearableProfileViewModel.state.isLoading
    )

    SimplePage(
        contentPadding = PaddingValues(
            bottom = 16.dp,
            end = 0.dp,
            start = 0.dp,
            top = 16.dp
        ),

        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = wearableProfileViewModel.state.wearable?.getName() ?: mac
            )
        }
    ) {
        WearableProfileHeader(wearableProfileViewModel)
        Spacer(Modifier.height(16.dp))

        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
        ) {
            WearableProfileOptions(wearableProfileViewModel)
        }
    }
}