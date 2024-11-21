package com.example.logifitappp.ui.screens.graphics

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.ui.components.headers.BottomTabsHeader
import com.example.logifitappp.ui.components.modals.MessageModal
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.views.AppViewModel
import com.example.logifitappp.viewmodel.views.GraphicsViewModel

@Composable
fun GraphicsView(
    appViewModel: AppViewModel,
    drawerState: DrawerState,
    navigation: NavHostController
) {
    val graphicsViewModel = hiltViewModel<GraphicsViewModel, GraphicsViewModel.GraphicsViewModelFactory>{
        it.create(appViewModel.user!!)
    }

    MessageModal(
        onClose = { graphicsViewModel.stopProcessing() },
        onDismissRequest = { graphicsViewModel.stopProcessing() },
        status = graphicsViewModel.state.status,
        visible = graphicsViewModel.state.isLoading
    )

    ScrollablePage(
        backgroundColor = MaterialTheme.colorScheme.surfaceContainerLowest,
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
                navigation = navigation
            )
        }

        item {
            Spacer(Modifier.height(16.dp))
            GraphicsStepsChart(dataset = graphicsViewModel.state.stepsDataset)
        }

        item {
            Spacer(Modifier.height(16.dp))
            GraphicsHeartRateChart(dataset = graphicsViewModel.state.heartRateDataSet)
        }
    }
}