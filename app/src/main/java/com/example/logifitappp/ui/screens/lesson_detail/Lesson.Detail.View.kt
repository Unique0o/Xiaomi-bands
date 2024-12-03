package com.example.logifitappp.ui.screens.lesson_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.LoaderPage
import com.example.logifitappp.ui.components.pages.NoInternetPage
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.LessonDetailViewModel

@Composable
fun LessonDetailView(
    appViewModel: AppViewModel,
    navigation: NavHostController,
    lessonId: Int
) {
    val lessonDetailViewModel = hiltViewModel<LessonDetailViewModel, LessonDetailViewModel.LessonDetailViewModelFactory>{
        it.create(lessonId, appViewModel.user)
    }

    val insets = WindowInsets.navigationBars.asPaddingValues()

    if (lessonDetailViewModel.state.isFetchingLessonInformation) {
        LoaderPage()
        return
    }

    if (lessonDetailViewModel.state.hasFetchLessonInformationFailed) {
        NoInternetPage { lessonDetailViewModel.fetchInformation() }
        return
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ScrollablePage(
            contentPadding = PaddingValues(
                bottom = 16.dp,
                end = 0.dp,
                start = 0.dp,
                top = 16.dp
            ),

            topBar = {
                ColumnStackHeader(
                    navigation = navigation,
                    title = lessonDetailViewModel.state.lesson?.name ?: ""
                )
            }
        ) {
            item {
                LessonDetailHeader(
                    lesson =  lessonDetailViewModel.state.lesson,
                    onEnd = { lessonDetailViewModel.markAsCompleted() }
                )
            }
        }

        LessonDetailFooter(
            lesson = lessonDetailViewModel.state.lesson,
            markAsCompleted = { lessonDetailViewModel.markAsCompleted() },
            modifier = Modifier
                .padding(bottom = insets.calculateBottomPadding() + 16.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
        )
    }
}