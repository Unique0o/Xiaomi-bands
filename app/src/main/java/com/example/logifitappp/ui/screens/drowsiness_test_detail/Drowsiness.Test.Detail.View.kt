package com.example.logifitappp.ui.screens.drowsiness_test_detail

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.ui.components.modals.MessageModal
import com.example.logifitappp.ui.components.pages.LoaderPage
import com.example.logifitappp.ui.components.pages.NoInternetPage
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.DrowsinessTestDetailViewModel
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DrowsinessTestDetailView(
    appViewModel: AppViewModel,
    navigation: NavHostController,
    drowsinessTestId: Int
) {
    val coroutineScope = rememberCoroutineScope()
    val drowsinessTestDetailViewModel = hiltViewModel<DrowsinessTestDetailViewModel, DrowsinessTestDetailViewModel.DrowsinessTestDetailViewModelFactory>{
        it.create(drowsinessTestId, appViewModel.user)
    }

    val pagerState = rememberPagerState(pageCount = {
        2
    })

    BackHandler(enabled = drowsinessTestDetailViewModel.state.currentPage == 1) {  }

    if (drowsinessTestDetailViewModel.state.result != null) {
        DrowsinessTestResult(
            navigation = navigation,
            result = drowsinessTestDetailViewModel.state.result!!,
            shouldItBackUntilHome = drowsinessTestDetailViewModel.state.shouldItBackUntilHome
        )

        return
    }

    if (drowsinessTestDetailViewModel.state.isFetchingTestDetail) {
        LoaderPage()
        return
    }

    if (drowsinessTestDetailViewModel.state.hasFetchTestDetailFailed) {
        NoInternetPage { drowsinessTestDetailViewModel.fetchTestDetail() }
        return
    }

    MessageModal(
        onClose = { drowsinessTestDetailViewModel.stopProcessing() },
        onDismissRequest = { drowsinessTestDetailViewModel.stopProcessing() },
        status = drowsinessTestDetailViewModel.state.status,
        visible = drowsinessTestDetailViewModel.state.isStoringResult
    )

    Surface {
        coroutineScope.launch {
            pagerState.animateScrollToPage(
                page = drowsinessTestDetailViewModel.state.currentPage
            )
        }

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> drowsinessTestDetailViewModel.state.data?.let {
                    DrowsinessTestQuestions(
                        answers = drowsinessTestDetailViewModel.state.answers,
                        canSign = drowsinessTestDetailViewModel.state.canSign,
                        data = it,
                        navigation = navigation,
                        onContinue = { drowsinessTestDetailViewModel.moveTo(1) },
                        onQuestionAnswered = { id, answer -> drowsinessTestDetailViewModel.handleQuestionAnswered(id, answer) },
                        onQuestionDetailChanged = { id, detail -> drowsinessTestDetailViewModel.handleQuestionDetailChanged(id, detail) }
                    )
                }

                1 -> DrowsinessTestSignature(
                    onBack = { drowsinessTestDetailViewModel.moveTo(0) },
                    onSigned = { drowsinessTestDetailViewModel.sign(it) }
                )
            }
        }
    }
}