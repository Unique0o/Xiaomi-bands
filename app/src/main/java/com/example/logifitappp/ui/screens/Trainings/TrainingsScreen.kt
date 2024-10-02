package com.example.logifitappp.ui.screens.Trainings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Trainings.TrainingItem
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme

data class Training(
    val title: String,
    val description: String,
    val imageRes: Int
)

@Composable
fun TrainingsScreen(
    navigation: NavHostController,
    trainings: List<Training>,
    onTrainingClick: (String) -> Unit
) {
    SimplePage(
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                ColumnStackHeader(
                    navigation = navigation,
                    title = stringResource(id = R.string.trainings)
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(trainings) { training ->
                        TrainingItem(
                            title = training.title,
                            description = training.description,
                            imageRes = training.imageRes,
                            onClick = { onTrainingClick(training.title) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }
    )

}

@Composable
@Preview
fun TrainingsScreenPreview() {
    LogifitApppTheme {
        val previewTrainings = listOf(
            Training("Smartband Logifit", "Logifit cuenta con una smar...", R.drawable.user1),
            Training("App Logifit", "Conoce en esta capacitació...", R.drawable.user1),
            Training("Plataforma de Gesti...", "En este módulo podrá desc...", R.drawable.user1),
            Training("Capacitación sobre F...", "Este módulo está diseñado...", R.drawable.user1),
            Training("Manual de Procedim...", "Este módulo ofrece una guí...", R.drawable.user1)
        )

        TrainingsScreen(
            navigation = rememberNavController(),
            trainings = previewTrainings,
            onTrainingClick = {}
        )
    }

}