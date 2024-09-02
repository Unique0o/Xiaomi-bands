package com.example.logifitappp.ui.screens.wearable_detection

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.BluetoothSearching
import androidx.compose.material.icons.rounded.StopCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.core.broadcasters.BluetoothBroadcastReceiver
import com.example.logifitappp.core.bluetooth.ScanEvent
import com.example.logifitappp.core.wearebles.WearableCandidate
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.viewmodel.views.WearableDetectionViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WearableDetectionView(
    navigation: NavHostController
) {
    val context = LocalContext.current
    val wearableDetectionViewModel: WearableDetectionViewModel = viewModel()
    val bluetoothPermissions = rememberMultiplePermissionsState(permissions = wearableDetectionViewModel.getWantedPermissions())

    DisposableEffect(context) {
        val receiver = object: BluetoothBroadcastReceiver() {
            override fun getCandidateByDevice(device: BluetoothDevice): WearableCandidate? {
                return wearableDetectionViewModel.getCandidateByDevice(device)
            }

            override fun handleStateChanged(state: Int) {
                wearableDetectionViewModel.handleBluetoothStateChanged(state)
            }

            override fun handleWearableFound(event: ScanEvent) {
                wearableDetectionViewModel.handleWearableFound(event)
            }
        }

        val bluetoothIntents = IntentFilter()
        bluetoothIntents.addAction(BluetoothDevice.ACTION_FOUND)
        bluetoothIntents.addAction(BluetoothDevice.ACTION_UUID)
        bluetoothIntents.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        bluetoothIntents.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        bluetoothIntents.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)

        ContextCompat.registerReceiver(context, receiver, bluetoothIntents, ContextCompat.RECEIVER_EXPORTED)

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        bluetoothPermissions.launchMultiplePermissionRequest()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        wearableDetectionViewModel.stopDiscovery()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        wearableDetectionViewModel.stopDiscovery()
    }

    SimplePage(
        content = {
            IconButton(
                icon = if (wearableDetectionViewModel.isScanning) Icons.Rounded.StopCircle else Icons.AutoMirrored.Rounded.BluetoothSearching,
                onClick = { wearableDetectionViewModel.toggleDiscovery() },
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = if (wearableDetectionViewModel.isScanning) R.string.button_stop_detection else R.string.button_start_detection)
            )

            WearableDetectionList(candidates = wearableDetectionViewModel.candidates) {
                println("candidate: ${it.getMacAddress()}")
            }

            if (wearableDetectionViewModel.isScanning) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceTint
                )
            }
        },

        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.button_device_detection)
            )
        }
    )
}

@Preview
@Composable
fun WearableDetectionPreview() {
    LogifitApppTheme {
        WearableDetectionView(rememberNavController())
    }
}