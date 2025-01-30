package com.example.logifitappp.ui.screens.synchronization_report

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.core.utils.plus
import com.example.logifitappp.ui.components.headers.BottomTabsHeader
import com.example.logifitappp.ui.components.modals.MessageModal
import com.example.logifitappp.ui.components.pages.NoInternetPage
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.SynchronizationReportViewModel

@Composable
fun SynchronizationReportView(
    appViewModel: AppViewModel,
    drawerState: DrawerState,
    navigation: NavHostController,
    contentPadding: PaddingValues
) {
    val synchronizationReportViewModel = hiltViewModel<SynchronizationReportViewModel, SynchronizationReportViewModel.SynchronizationReportViewModelFactory> {
        it.create(appViewModel.user)
    }

    MessageModal(
        onClose = { synchronizationReportViewModel.stopProcessing() },
        onDismissRequest = { synchronizationReportViewModel.stopProcessing() },
        status = synchronizationReportViewModel.state.status,
        visible = synchronizationReportViewModel.state.isFetchingReport
    )

    if (synchronizationReportViewModel.state.hasFetchReportFailed) {
        NoInternetPage(
            topBar = {
                BottomTabsHeader(
                    appViewModel = appViewModel,
                    drawerState = drawerState,
                    navigation = navigation
                )
            }
        ) { synchronizationReportViewModel.fetchReport() }

        return
    }

    ScrollablePage(
        contentPadding = PaddingValues(
            bottom = 0.dp,
            end = 0.dp,
            start = 0.dp,
            top = 16.dp
        ) + contentPadding,
        topBar = {
            BottomTabsHeader(
                appViewModel = appViewModel,
                drawerState = drawerState,
                navigation = navigation
            )
        }
    ) {
        item {
            SynchronizationReportResume(synchronizationReportViewModel.state)
            SynchronizationReportFilters(synchronizationReportViewModel)

            SynchronizationReportConditionList(
                conditions = synchronizationReportViewModel.state.conditions,
                currentTabIndex = synchronizationReportViewModel.state.currentTabIndex,
                onSelectConditionIndex = { synchronizationReportViewModel.updateConditionIndex(it) }
            )
        }

        item {
            Spacer(Modifier.height(16.dp))

            SynchronizationReportTeam(
                items = synchronizationReportViewModel.state.filteredReport[synchronizationReportViewModel.state.currentTabIndex],
                onShareWeeklyReport = { synchronizationReportViewModel.shareWeeklyReport() }
            )
        }
    }
}