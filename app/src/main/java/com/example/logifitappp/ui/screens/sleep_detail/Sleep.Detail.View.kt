package com.example.logifitappp.ui.screens.sleep_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.graphics.SimpleHorizontalStackBar
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.SleepDetailViewModel

@Composable
fun SleepDetailView(
    appViewModel: AppViewModel,
    navigation: NavHostController,
    mac: String
) {
    val sleepDetailViewModel = hiltViewModel<SleepDetailViewModel, SleepDetailViewModel.SleepDetailViewModelFactory>{
        it.create(mac, appViewModel.user)
    }

    ScrollablePage(
        contentPadding = PaddingValues(0.dp),
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.sleep_detail_title)
            )
        }
    ) {
        item {
            SleepDetailHeader(sleepDetailViewModel)
            Spacer(Modifier.height(16.dp))
            SimpleHorizontalStackBar(dataSet = sleepDetailViewModel.state.sleepDataSet)
        }

        if (!sleepDetailViewModel.state.sleepDataSet.empty) {
            item {
                Column(Modifier.padding(vertical = 16.dp, horizontal = 12.dp)) {
                    SleepDetailSummary(sleepDetailViewModel)
                }
            }
        }
    }
}