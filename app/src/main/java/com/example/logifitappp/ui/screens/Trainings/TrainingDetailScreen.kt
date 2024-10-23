package com.example.logifitappp.ui.screens.Trainings


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.data.models.LessonModel
import com.example.logifitappp.data.models.TrainingInfoModel
import com.example.logifitappp.ui.components.Trainings.TrainingDetailContent
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.viewmodel.views.Trainings.TrainingDetailUiState
import com.example.logifitappp.viewmodel.views.Trainings.TrainingDetailViewModel


@Composable
fun TrainingDetailScreen(
    navigation: NavHostController,
    trainingId: String
) {
    val viewModel: TrainingDetailViewModel = hiltViewModel()
    val selectedTraining by viewModel.selectedTraining.collectAsState()
    val lessonsState by viewModel.lessonsState.collectAsState()

    LaunchedEffect(trainingId) {
        viewModel.loadTrainingDetails(trainingId)
    }

    SimplePage(
        content = {
            when {
                selectedTraining != null && lessonsState is TrainingDetailUiState.Success -> {
                    val lessons = (lessonsState as TrainingDetailUiState.Success).data
                    TrainingDetailContent(training = selectedTraining!!, lessons = lessons)
                }
                lessonsState is TrainingDetailUiState.Error -> {
                    Text("Error: ${(lessonsState as TrainingDetailUiState.Error).message}")
                }
                selectedTraining == null -> {
                    Text("Training not found")
                }
            }
        },
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = selectedTraining?.title ?: stringResource(id = R.string.smartband_logifit)
            )
        }
    )
}

@Preview
@Composable
fun TrainingDetailScreenPreview() {
    LogifitApppTheme {
    val sampleTraining = TrainingInfoModel(
        id = "1001",
        title = "Smartband Logifit",
        description = "Aprende sobre tu Smartband Logifit y cómo puede ayudarte a mejorar tu salud y bienestar.",
        imageRes = R.drawable.user1
    )
    val sampleLessons = listOf(
        LessonModel(
            id = 1,
            name = "Qué es una Smartband",
            description = "Introducción a las pulseras inteligentes",
            duration = 88,
            externalIdentifier = 101,
            isCompleted = false,
            path = "/lessons/smartband_intro",
            trainingExternalIdentifier = 1001,
            videoUrl = "https://example.com/videos/smartband_intro.mp4",
            imageRes = R.drawable.user1
        ),
        LessonModel(
            id = 1,
            name = "Qué es una Smartband",
            description = "Introducción a las pulseras inteligentes",
            duration = 88,
            externalIdentifier = 101,
            isCompleted = false,
            path = "/lessons/smartband_intro",
            trainingExternalIdentifier = 1001,
            videoUrl = "https://example.com/videos/smartband_intro.mp4",
            imageRes = R.drawable.user1
        ),
    )
        TrainingDetailScreen(rememberNavController(), "1001")
    }
}