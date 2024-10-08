package com.example.logifitappp.ui.screens.Trainings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Trainings.TrainingItem
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.viewmodel.views.Trainings.TrainingInfoUiState
import com.example.logifitappp.viewmodel.views.Trainings.TrainingViewModel


@Composable
fun TrainingsScreen(
    navigation: NavHostController
) {
    val viewModel: TrainingViewModel = hiltViewModel()
    val trainings by viewModel.trainingInfo.collectAsState()

    SimplePage(
        content = {
            when (trainings) {
                TrainingInfoUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                }
                is TrainingInfoUiState.Success -> {
                    val info = (trainings as TrainingInfoUiState.Success).data
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(info.size) { index ->
                            val item = info[index]
                            TrainingItem(
                                title = item.title,
                                description = item.description,
                                imageRes = item.imageRes,
                                onClick = { viewModel.onTrainingClick(/* */) }
                            )
                        }
                    }
                }
                is TrainingInfoUiState.Error -> {
                    Text(
                        text = (trainings as TrainingInfoUiState.Error).message,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.trainings)
            )
        }
    )
}
