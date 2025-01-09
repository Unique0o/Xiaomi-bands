package com.example.logifitappp.ui.screens.additional_information

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.logifitappp.ui.components.modals.MessageModal
import com.example.logifitappp.ui.components.pages.LoaderPage
import com.example.logifitappp.ui.components.pages.NoInternetPage
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.AdditionalInformationViewModel
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AdditionalInformationView(
    appViewModel: AppViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = {
        2
    })

    val additionalInformationViewModel = hiltViewModel<AdditionalInformationViewModel, AdditionalInformationViewModel.AdditionalInformationViewModelFactory>{
        it.create(appViewModel.user)
    }

    if (additionalInformationViewModel.state.isNecessaryDataFetching) {
        LoaderPage()
        return
    }

    if (additionalInformationViewModel.state.hasNecessaryDataFetchingFailed) {
        NoInternetPage { additionalInformationViewModel.fetchNecessaryData() }
        return
    }

    MessageModal(
        onClose = { additionalInformationViewModel.stopProcessing() },
        onDismissRequest = { additionalInformationViewModel.stopProcessing() },
        status = additionalInformationViewModel.state.storingInformationStatus,
        visible = additionalInformationViewModel.state.isInformationStoring
    )

    Surface {
        coroutineScope.launch {
            pagerState.animateScrollToPage(
                page = additionalInformationViewModel.state.currentPage
            )
        }

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> AdditionalInformationForm(additionalInformationViewModel)
                1 -> AdditionalInformationProfilePhoto(additionalInformationViewModel)
            }
        }
    }
}