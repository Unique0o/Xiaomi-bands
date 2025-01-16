package com.example.logifitappp.ui.screens.graphics

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavHostController
import com.example.logifitappp.core.utils.avoidBottom
import com.example.logifitappp.core.utils.plus
import com.example.logifitappp.ui.components.headers.BottomTabsHeader
import com.example.logifitappp.ui.components.modals.MessageModal
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.GraphicsViewModel

@Composable
fun GraphicsView(
    appViewModel: AppViewModel,
    drawerState: DrawerState,
    navigation: NavHostController,
    contentPadding: PaddingValues? = null
) {
    val graphicsViewModel = hiltViewModel<GraphicsViewModel, GraphicsViewModel.GraphicsViewModelFactory>{
        it.create(appViewModel.user)
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        graphicsViewModel.refreshGraphics(graphicsViewModel.state.shift, graphicsViewModel.state.wearable)
    }

    MessageModal(
        onClose = { graphicsViewModel.stopProcessing() },
        onDismissRequest = { graphicsViewModel.stopProcessing() },
        status = graphicsViewModel.state.status,
        visible = graphicsViewModel.state.isLoading
    )

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
        item {
            GraphicsSentMessage(graphicsViewModel = graphicsViewModel)
        }

        item {
            Spacer(Modifier.height(16.dp))

            GraphicsSleepChart(
                dataSet = graphicsViewModel.state.sleepDataSet,
                shift = graphicsViewModel.state.shift,
                navigation = navigation,
                wearable = graphicsViewModel.state.wearable
            )
        }

        graphicsViewModel.state.wearable?.getWearableCoordinator()?.let {
            if (it.supportsStepCounter()) {
                item {
                    Spacer(Modifier.height(16.dp))

                    GraphicsStepsChart(
                        dataset = graphicsViewModel.state.stepsDataset,
                        navigation = navigation,
                        wearable = graphicsViewModel.state.wearable
                    )
                }
            }

            if (it.supportsHeartRateMeasurement()) {
                item {
                    Spacer(Modifier.height(16.dp))

                    GraphicsHeartRateChart(
                        dataset = graphicsViewModel.state.heartRateDataSet,
                        navigation = navigation,
                        wearable = graphicsViewModel.state.wearable
                    )
                }
            }

            if (it.supportsSpo2()) {
                item {
                    Spacer(Modifier.height(16.dp))

                    GraphicsSpo2Chart(
                        dataset = graphicsViewModel.state.spo2DataSet,
                        navigation = navigation,
                        wearable = graphicsViewModel.state.wearable
                    )
                }
            }
        }

    }
}