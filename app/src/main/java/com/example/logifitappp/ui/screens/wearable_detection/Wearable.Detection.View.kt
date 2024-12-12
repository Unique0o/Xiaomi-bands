package com.example.logifitappp.ui.screens.wearable_detection

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavHostController
import com.example.logifitappp.core.App
import com.example.logifitappp.core.broadcasters.BluetoothBroadcastReceiver
import com.example.logifitappp.core.bluetooth.ScanEvent
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableCandidate
import com.example.logifitappp.core.wearebles.WearableManager
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.ui.components.modals.MessageModal
import com.example.logifitappp.viewmodel.views.WearableDetectionViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WearableDetectionView(
    navigation: NavHostController
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val wearableDetectionViewModel = hiltViewModel<WearableDetectionViewModel, WearableDetectionViewModel.WearableDetectionViewModelFactory>{
        it.create(navigation)
    }

    val bluetoothPermissions = rememberMultiplePermissionsState(permissions = wearableDetectionViewModel.getWantedPermissions())
    val pagerState = rememberPagerState(pageCount = {
        2
    })

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

    DisposableEffect(Unit) {
        bluetoothPermissions.launchMultiplePermissionRequest()

        val receiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                when (intent.action) {
                    WearableManager.ACTION_DEVICES_CHANGED -> wearableDetectionViewModel.checkWearableConnection()

                    App.FAILED_CONNECTION_WITH_WEARABLE -> {
                        val code = intent.getIntExtra(Wearable.EXTRA_FAILED_CONNECTION_STATUS, -1)
                        wearableDetectionViewModel.handleFailedConnection(AppStatusCodeEnum.fromCode(code))
                    }
                }
            }
        }

        val filterLocal = IntentFilter()
        filterLocal.addAction(WearableManager.ACTION_DEVICES_CHANGED)
        filterLocal.addAction(App.FAILED_CONNECTION_WITH_WEARABLE)
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filterLocal)

        onDispose {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        wearableDetectionViewModel.stopDiscovery()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        wearableDetectionViewModel.stopDiscovery()
    }

    BackHandler(enabled = wearableDetectionViewModel.state.currentPage == 1) {  }

    MessageModal(
        onClose = { wearableDetectionViewModel.stopProcessing() },
        onDismissRequest = { wearableDetectionViewModel.stopProcessing() },
        status = wearableDetectionViewModel.state.status ?: AppStatusCodeEnum.INVALID_WEARABLE_AUTHENTICATION_KEY,
        visible = wearableDetectionViewModel.state.status != null
    )

    WearableDetectionAuthenticationBottomSheet(
        authenticate = { wearableDetectionViewModel.authenticate() },
        wearableDetectionViewModel = wearableDetectionViewModel
    )

    Surface {
        coroutineScope.launch {
            pagerState.animateScrollToPage(
                page = wearableDetectionViewModel.state.currentPage
            )
        }

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> WearableDetectionStep(
                    navigation = navigation,
                    wearableDetectionViewModel = wearableDetectionViewModel
                )

                1 -> WearableDetectionPairing(wearableDetectionViewModel.state.currentCandidate)
            }
        }
    }
}