package com.example.logifitappp.ui.screens.Trainings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.data.models.LessonModel
import com.example.logifitappp.data.models.TrainingInfoModel
import com.example.logifitappp.ui.components.Trainings.DescriptionSection
import com.example.logifitappp.ui.components.Trainings.LessonItem
import com.example.logifitappp.ui.components.Trainings.ProgressSection
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.viewmodel.views.Trainings.TrainingDetailViewModel


@Composable
fun TrainingDetailScreen(
    navigation: NavHostController,
    trainingId: String
) {
    val viewModel: TrainingDetailViewModel = hiltViewModel()
    val trainingState by viewModel.trainingState.collectAsState()
    val lessonsState by viewModel.lessonsState.collectAsState()

    LaunchedEffect(trainingId) {
        viewModel.loadTrainingDetails(trainingId)
    }

    SimplePage(
        content = {
            trainingState?.let { training ->
                TrainingDetailContent(training, lessonsState)
            } ?: run {
                CircularProgressIndicator()
            }
        },
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = trainingState?.title ?: stringResource(id = R.string.smartband_logifit)
            )
        }
    )
}

@Composable
fun TrainingDetailContent(training: TrainingInfoModel, lessons: List<LessonModel>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            Image(
                painter = painterResource(id = training.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }
        item { DescriptionSection(description = training.description) }
        item { ProgressSection() }
        item {
            Text(
                text = stringResource(id = R.string.lessons),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )
        }
        items(lessons) { lesson ->
            LessonItem(lesson = lesson, onClick = {

            })
        }
    }
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