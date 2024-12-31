package com.example.logifitappp.ui.screens.wearable_profile

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavHostController
import com.example.logifitappp.core.App
import com.example.logifitappp.core.utils.parcelableExtra
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableManager
import com.example.logifitappp.enums.AppStatusCodeEnum
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
    val context = LocalContext.current
    val wearableProfileViewModel = hiltViewModel<WearableProfileViewModel, WearableProfileViewModel.WearableProfileViewModelFactory>{
        it.create(mac, appViewModel.user)
    }

    DisposableEffect(Unit) {
        val receiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                when (intent.action) {
                    App.ACTION_NEW_DATA -> {
                        val wearable = intent.parcelableExtra<Wearable>(Wearable.EXTRA_DEVICE)!!
                        wearableProfileViewModel.refreshSingleWearable(wearable)
                    }

                    App.FAILED_CONNECTION_WITH_WEARABLE -> {
                        val code = intent.getIntExtra(Wearable.EXTRA_FAILED_CONNECTION_STATUS, -1)
                        wearableProfileViewModel.handleFailedConnection(AppStatusCodeEnum.fromCode(code))
                    }

                    WearableManager.ACTION_DEVICES_CHANGED -> {
                        wearableProfileViewModel.checkWearableConnection()
                    }
                }
            }
        }

        val filterLocal = IntentFilter()
        filterLocal.addAction(App.ACTION_NEW_DATA)
        filterLocal.addAction(App.FAILED_CONNECTION_WITH_WEARABLE)
        filterLocal.addAction(WearableManager.ACTION_DEVICES_CHANGED)
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filterLocal)

        onDispose {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
        }
    }

    MessageModal(
        onClose = { wearableProfileViewModel.stopProcessing() },
        onDismissRequest = { wearableProfileViewModel.stopProcessing() },
        status = wearableProfileViewModel.state.status,
        visible = wearableProfileViewModel.state.isLoading
    )

    SimplePage(
        contentPadding = PaddingValues(
            bottom = 0.dp,
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
            Spacer(Modifier.weight(1f))
            WearableProfileActions(wearableProfileViewModel)
        }
    }
}