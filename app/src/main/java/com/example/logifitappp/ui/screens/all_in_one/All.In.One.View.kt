package com.example.logifitappp.ui.screens.all_in_one

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.utils.avoidBottom
import com.example.logifitappp.core.utils.parcelableExtra
import com.example.logifitappp.core.utils.plus
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableManager
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.headers.BottomTabsHeader
import com.example.logifitappp.ui.components.modals.ChangeShiftModal
import com.example.logifitappp.ui.components.modals.MessageModal
import com.example.logifitappp.ui.components.modals.UnpairWearableModal
import com.example.logifitappp.ui.components.pages.IconMessagePage
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.AllInOneViewModel

@Composable
fun AllInOneView(
    appViewModel: AppViewModel,
    drawerState: DrawerState,
    navigation: NavHostController,
    contentPadding: PaddingValues
) {
    val context = LocalContext.current
    val allInOneViewModel = hiltViewModel<AllInOneViewModel, AllInOneViewModel.AllInOneViewModelFactory> {
        it.create(navigation, appViewModel.user, appViewModel.tenant)
    }

    DisposableEffect(Unit) {
        val receiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                when (intent.action) {
                    App.ACTION_NEW_DATA -> {
                        val wearable = intent.parcelableExtra<Wearable>(Wearable.EXTRA_DEVICE)!!
                        allInOneViewModel.refreshSingleWearable(wearable)
                    }

                    WearableManager.ACTION_DEVICES_CHANGED -> {
                        allInOneViewModel.checkWearableConnection()
                    }
                }
            }
        }

        val filterLocal = IntentFilter()
        filterLocal.addAction(App.ACTION_NEW_DATA)
        filterLocal.addAction(WearableManager.ACTION_DEVICES_CHANGED)
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filterLocal)

        onDispose {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
        }
    }

    MessageModal(
        onClose = { allInOneViewModel.stopFetchingWorkersProcessing() },
        onDismissRequest = { allInOneViewModel.stopFetchingWorkersProcessing() },
        status = allInOneViewModel.state.status,
        visible = allInOneViewModel.state.isLoading
    )

    UnpairWearableModal(
        onClose = { allInOneViewModel.closeUnpairWearableModal() },
        onDismissRequest = { allInOneViewModel.closeUnpairWearableModal() },
        onUnpairedWearable = { allInOneViewModel.handleUnpairWearable(it) },
        visible = allInOneViewModel.state.shouldShowUnpairWearableModal,
        wearable = allInOneViewModel.state.currentWearable
    )

    ChangeShiftModal(
        onClose = { allInOneViewModel.closeWearableShiftModal() },
        onDismissRequest = { allInOneViewModel.closeWearableShiftModal() },
        onSelectShift = { allInOneViewModel.handleSelectShift(it) },
        shiftId = allInOneViewModel.state.currentShift?.id,
        visible = allInOneViewModel.state.shouldShowWearableShiftModal
    )

    if (allInOneViewModel.filteredWearables.isEmpty()) {
        IconMessagePage(
            action = { navigation.navigate(MainRoutes.WearableDetection) },
            buttonIcon = Icons.Default.Add,
            buttonLabel = stringResource(id = R.string.button_pair_smart_band),
            message = stringResource(R.string.no_paired_wearables_page_message),
            pageIcon = Icons.Default.Watch,
            topBar = {
                BottomTabsHeader(
                    appViewModel = appViewModel,
                    drawerState = drawerState,
                    navigation = navigation
                )
            }
        )

        return
    }

    ScrollablePage(
        contentPadding = PaddingValues(16.dp).avoidBottom() + contentPadding,
        topBar = {
            BottomTabsHeader(
                appViewModel = appViewModel,
                drawerState = drawerState,
                navigation = navigation
            )
        }
    ) {
        item { AllInOneHeader(allInOneViewModel, navigation) }

        items(allInOneViewModel.filteredWearables) {
            AllInOneWearableItem(allInOneViewModel, navigation, it)
            Spacer(Modifier.height(16.dp))
        }
    }
}