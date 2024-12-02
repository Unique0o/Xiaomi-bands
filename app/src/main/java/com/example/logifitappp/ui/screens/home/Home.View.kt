package com.example.logifitappp.ui.screens.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.utils.AndroidUtils
import com.example.logifitappp.core.utils.parcelableExtra
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableManager
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.components.forms.RadioButtonGroup
import com.example.logifitappp.ui.components.headers.BottomTabsHeader
import com.example.logifitappp.ui.components.modals.MessageModal
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.ui.components.screenshots.SleepDetailScreenshot
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.HomeViewModel

@Composable
fun HomeView(
    appViewModel: AppViewModel,
    drawerState: DrawerState,
    navigation: NavHostController
) {
    val context = LocalContext.current
    val homeViewModel = hiltViewModel<HomeViewModel, HomeViewModel.HomeViewModelFactory>{
        it.create(appViewModel.user!!)
    }

    DisposableEffect(Unit) {
        val receiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                when (intent.action) {
                    App.ACTION_NEW_DATA -> {
                        val wearable = intent.parcelableExtra<Wearable>(Wearable.EXTRA_DEVICE)!!
                        homeViewModel.refreshSingleWearable(wearable)
                    }

                    App.AUTHENTICATION_KEY_FAILED -> {
                        homeViewModel.handleAuthenticationKeyFailed()
                    }

                    WearableManager.ACTION_DEVICES_CHANGED -> {
                        homeViewModel.checkWearableConnection()
                    }
                }
            }
        }

        val filterLocal = IntentFilter()
        filterLocal.addAction(App.ACTION_NEW_DATA)
        filterLocal.addAction(App.AUTHENTICATION_KEY_FAILED)
        filterLocal.addAction(WearableManager.ACTION_DEVICES_CHANGED)
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filterLocal)

        onDispose {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
        }
    }

    MessageModal(
        onClose = { homeViewModel.stopProcessing() },
        onDismissRequest = { homeViewModel.stopProcessing() },
        status = homeViewModel.state.status,
        visible = homeViewModel.state.isLoading
    ) {
        if (homeViewModel.state.status == AppStatusCodeEnum.UNSELECTED_SHIFT) {
            Column {
                Spacer(Modifier.height(16.dp))

                RadioButtonGroup(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onChangeValue = { homeViewModel.shift = it },
                    options = homeViewModel.shifts,
                    value = homeViewModel.shift
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.change_shift_modal_message),
                    textAlign = TextAlign.Center,
                    typography = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    enabled = homeViewModel.shift != null,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        homeViewModel.handleChangeShift(homeViewModel.shift!!)
                        homeViewModel.fetchActivities(homeViewModel.wearables.first())
                    },
                    text = stringResource(id = R.string.button_continue)
                )
            }
        }
    }

    AndroidUtils.CaptureComposableAsBitmap({ bitmap ->  homeViewModel.bitmap = bitmap }) {
        SleepDetailScreenshot(
            drowsiness = homeViewModel.state.drowsiness,
            drowsinessCondition = homeViewModel.state.drowsinessCondition,
            fatigue = homeViewModel.state.fatigue,
            shift = homeViewModel.state.shift,
            tenant = homeViewModel.state.tenant,
            user = appViewModel.user!!
        )
    }

    ScrollablePage(
        topBar = {
            BottomTabsHeader(
                appViewModel = appViewModel,
                drawerState = drawerState,
                navigation = navigation
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                    HomeShiftCard(
                        onSelectShift = { homeViewModel.handleChangeShift(it) },
                        shift = homeViewModel.state.shift
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (homeViewModel.state.tenant?.shouldItShowDrowsinessTest == true) {
                        HomeLocationCard(
                            onSelectLocation = { homeViewModel.handleChangeLocation(it) },
                            location = homeViewModel.state.location
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    ) {
        item {
            HomeWearable(
                homeViewModel = homeViewModel,
                navigation = navigation
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            HomeDrowsinessTest(
                homeViewModel = homeViewModel,
                navigation = navigation
            )
        }
    }
}