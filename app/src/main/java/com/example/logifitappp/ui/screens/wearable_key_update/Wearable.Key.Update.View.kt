package com.example.logifitappp.ui.screens.wearable_key_update

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.modals.MessageModal
import com.example.logifitappp.ui.components.pages.LoaderPage
import com.example.logifitappp.ui.components.pages.NoInternetPage
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.WearableKeyUpdateViewModel

@Composable
fun WearableKeyUpdateView(
    appViewModel: AppViewModel,
    navigation: NavHostController
) {
    val wearableKeyUpdateViewModel = hiltViewModel<WearableKeyUpdateViewModel, WearableKeyUpdateViewModel.WearableKeyUpdateViewModelFactory> {
        it.create(appViewModel.tenant)
    }

    if (wearableKeyUpdateViewModel.state.isNecessaryDataFetching) {
        LoaderPage()
        return
    }

    if (wearableKeyUpdateViewModel.state.hasNecessaryDataFetchingFailed) {
        NoInternetPage { wearableKeyUpdateViewModel.fetchNecessaryData() }
        return
    }

    MessageModal(
        onClose = { wearableKeyUpdateViewModel.stopProcessing() },
        onDismissRequest = { wearableKeyUpdateViewModel.stopProcessing() },
        status = wearableKeyUpdateViewModel.state.status,
        visible = wearableKeyUpdateViewModel.state.isLoading
    )

    ScrollablePage(
        contentPadding = PaddingValues(0.dp),
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.wearable_key_update_title)
            )
        }
    ) {
        item {
            WearableKeyUpdateFilters(wearableKeyUpdateViewModel)
        }

        item {
            if (wearableKeyUpdateViewModel.macs.isNotEmpty()) WearableKeyUpdateMacList(wearableKeyUpdateViewModel)
        }
    }
}