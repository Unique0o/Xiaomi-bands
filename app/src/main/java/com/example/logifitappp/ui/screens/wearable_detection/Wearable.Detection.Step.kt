package com.example.logifitappp.ui.screens.wearable_detection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.BluetoothSearching
import androidx.compose.material.icons.rounded.StopCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Loader
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.views.WearableDetectionViewModel

@Composable
fun WearableDetectionStep(
    navigation: NavHostController,
    wearableDetectionViewModel: WearableDetectionViewModel
) {
    ScrollablePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.button_device_detection)
            )
        }
    ) {
        item {
            IconButton(
                icon = if (wearableDetectionViewModel.state.isScanning) Icons.Rounded.StopCircle else Icons.AutoMirrored.Rounded.BluetoothSearching,
                onClick = { wearableDetectionViewModel.toggleDiscovery() },
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = if (wearableDetectionViewModel.state.isScanning) R.string.button_stop_detection else R.string.button_start_detection)
            )

            Spacer(Modifier.height(8.dp))
        }

        items(wearableDetectionViewModel.candidates) { candidate ->
            WearableDetectionListItem(
                candidate = candidate,
                onCandidatePressed = { wearableDetectionViewModel.handleCandidatePressed(it) }
            )

            Spacer(Modifier.height(16.dp))
        }

        if (wearableDetectionViewModel.state.isScanning) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Loader()
                }
            }
        }
    }
}