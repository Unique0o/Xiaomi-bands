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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Trainings.DescriptionSection
import com.example.logifitappp.ui.components.Trainings.LessonItem
import com.example.logifitappp.ui.components.Trainings.ProgressSection
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme

data class Lesson(
    val title: String,
    val description: String,
    val duration: String,
    val imageRes: Int
)

@Composable
fun TrainingDetailScreen(
    navigation: NavHostController,
    lessons: List<Lesson>
) {
    SimplePage(
        content = {
            Column(modifier = Modifier.fillMaxSize()) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    item {
                        Image(
                            painter = painterResource(id = R.drawable.user1),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    item { DescriptionSection() }
                    item { ProgressSection() }
                    item {
                        Text(
                            text = stringResource(id = R.string.lessons),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                        )
                    }
                    items(lessons) { lesson ->
                        LessonItem(lesson)
                    }
                }
            }
        },
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.smartband_logifit)
            )
        }
    )

}

@Preview
@Composable
fun TrainingDetailScreenPreview() {
    LogifitApppTheme {
        val lessons = listOf(
            Lesson(
                stringResource(R.string.what_is_smartband),
                stringResource(R.string.what_is_smartband_desc),
                "1MIN 28S",
                R.drawable.user1
            ),
            Lesson(
                stringResource(R.string.understanding_use),
                stringResource(R.string.understanding_use_desc),
                "1MIN 23S",
                R.drawable.user1
            ),
            Lesson(
                stringResource(R.string.how_to_link),
                stringResource(R.string.how_to_link_desc),
                "1MIN 40S",
                R.drawable.user1
            )
        )
        TrainingDetailScreen(rememberNavController(), lessons)
    }

}