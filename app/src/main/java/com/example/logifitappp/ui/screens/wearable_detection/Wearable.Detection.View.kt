package com.example.logifitappp.ui.screens.wearable_detection

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.BluetoothSearching
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material.icons.rounded.StopCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.RecordedDataTypesEnum
import com.example.logifitappp.core.broadcasters.BluetoothBroadcastReceiver
import com.example.logifitappp.core.bluetooth.ScanEvent
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableCandidate
import com.example.logifitappp.core.wearebles.WearableManager
import com.example.logifitappp.core.wearebles.WearableUpdateSubjectEnum
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.viewmodel.views.WearableDetectionViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.util.Locale
import kotlin.math.floor

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

        val receiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                when (intent.action) {
                    App.ACTION_NEW_DATA -> {
                        val wearable = intent.getParcelableExtra<Wearable>(Wearable.EXTRA_DEVICE)!!
                        wearableDetectionViewModel.refreshSingleWearable(wearable)
                    }

                    WearableManager.ACTION_DEVICES_CHANGED -> wearableDetectionViewModel.refreshPairedWearables()
                }
            }
        }

        val filterLocal = IntentFilter()
        filterLocal.addAction(App.ACTION_NEW_DATA)
        filterLocal.addAction(WearableManager.ACTION_DEVICES_CHANGED)
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filterLocal)

        wearableDetectionViewModel.refreshPairedWearables()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        wearableDetectionViewModel.stopDiscovery()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        wearableDetectionViewModel.stopDiscovery()
    }

    SimplePage(
        content = {
            /*IconButton(
                icon = if (wearableDetectionViewModel.isScanning) Icons.Rounded.StopCircle else Icons.AutoMirrored.Rounded.BluetoothSearching,
                onClick = { wearableDetectionViewModel.toggleDiscovery() },
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = if (wearableDetectionViewModel.isScanning) R.string.button_stop_detection else R.string.button_start_detection)
            )*/

           /*WearableDetectionList(candidates = wearableDetectionViewModel.candidates) {
                wearableDetectionViewModel.handleCandidatePressed(it)
            }*/

            LazyColumn {
                items(wearableDetectionViewModel.wearables) { wearable ->
                    Column {
                        Text(text = wearable.getAliasOrName())

                        if (!wearable.isConnected() && !wearable.isInitialized()) {
                            Button(
                                onClick = {
                                    if (!wearable.getWearableCoordinator().isConnectable()) {
                                        wearable.setState(Wearable.State.WAITING_FOR_SCAN)
                                        wearable.sendDeviceUpdateIntent(App.context, WearableUpdateSubjectEnum.CONNECTION_STATE)

                                        return@Button
                                    }

                                    App.getWearableServiceTo(wearable).connect()
                                },
                                text = "Conectar"
                            )
                        } else if (wearable.isInitialized() && wearable.getWearableCoordinator().supportsActivityDataFetching()) {
                            Button(
                                onClick = {
                                    App.getWearableServiceTo(wearable).onFetchRecordedData(RecordedDataTypesEnum.TYPE_SYNC)
                                },
                                text = "Extraer"
                            )
                        }

                        wearableDetectionViewModel.sleeps.forEach {
                            Column {
                                Text(text = "start: ${DateTimeUtils.formatDateTime(it.startDate)}")
                                Text(text = "end: ${DateTimeUtils.formatDateTime(it.endDate)}")
                                Text(text = "time: ${String.format(Locale.ROOT, "%d:%02d", floor(it.totalMinutes / 60f).toInt(), (it.totalMinutes % 60f).toInt())}")
                                Text(text = "type: ${it.activityType.name}")
                            }
                        }
                    }
                }

                item {
                    IconButton(
                        icon = if (wearableDetectionViewModel.isScanning) Icons.Rounded.StopCircle else Icons.AutoMirrored.Rounded.BluetoothSearching,
                        onClick = { wearableDetectionViewModel.toggleDiscovery() },
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = if (wearableDetectionViewModel.isScanning) R.string.button_stop_detection else R.string.button_start_detection)
                    )
                }

                items(wearableDetectionViewModel.candidates) {candidate ->
                    Row(
                        modifier = Modifier.clickable { wearableDetectionViewModel.handleCandidatePressed(candidate) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Watch,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Text(
                                color = MaterialTheme.colorScheme.surfaceTint,
                                text = candidate.getName(),
                                typography = MaterialTheme.typography.titleLarge
                            )

                            Text(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                text = candidate.getMacAddress(),
                                typography = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            imageVector = Icons.Outlined.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                item {
                    if (wearableDetectionViewModel.isScanning) {
                        CircularProgressIndicator(
                            modifier = Modifier.width(64.dp),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceTint
                        )
                    }
                }
            }

            /*if (wearableDetectionViewModel.isScanning) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceTint
                )
            }*/
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