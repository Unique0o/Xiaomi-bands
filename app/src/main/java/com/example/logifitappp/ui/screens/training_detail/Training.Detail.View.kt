package com.example.logifitappp.ui.screens.training_detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.logifitappp.core.utils.DurationUtils
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.ProgressiveImage
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.modals.MessageModal
import com.example.logifitappp.ui.components.pages.LoaderPage
import com.example.logifitappp.ui.components.pages.NoInternetPage
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.viewmodel.views.TrainingDetailViewModel
import com.google.gson.Gson

@Composable
fun TrainingDetailView(
    backStackEntry: NavBackStackEntry,
    navigation: NavHostController,
    trainingId: Int
) {
    val gson = Gson()
    val trainingDetailViewModel = hiltViewModel<TrainingDetailViewModel, TrainingDetailViewModel.TrainingDetailViewModelFactory>{
        it.create(trainingId)
    }

    if (trainingDetailViewModel.state.isFetchingTrainingInformation) {
        LoaderPage()
        return
    }

    if (trainingDetailViewModel.state.hasFetchTrainingInformationFailed) {
        NoInternetPage { trainingDetailViewModel.fetchInformation() }
        return
    }

    MessageModal(
        onClose = { trainingDetailViewModel.stopProcessing() },
        onDismissRequest = { trainingDetailViewModel.stopProcessing() },
        status = trainingDetailViewModel.state.status,
        visible = trainingDetailViewModel.state.isDownloadingCertificate
    )

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
                title = trainingDetailViewModel.state.training?.name ?: ""
            )
        }
    ) {
        item { TrainingDetailHeader(trainingDetailViewModel) }

        items(trainingDetailViewModel.lessons) { lesson ->
            Row(
                Modifier
                    .height(100.dp)
                    .padding(horizontal = 16.dp)
                    .clickable {
                        navigation.navigate(MainRoutes.LessonDetail(
                            lesson.id,
                            gson.toJson(trainingDetailViewModel.lessons.map { it.id }.toTypedArray())
                        ))
                    }
            ) {
                ProgressiveImage(
                    modifier = Modifier.width(130.dp)
                        .fillMaxHeight()
                        .defaultMinSize(minHeight = 70.dp),
                    url = lesson.image
                )

                Spacer(Modifier.width(8.dp))

                Column(
                    Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = lesson.name,
                        typography = MaterialTheme.typography.displayMedium
                    )

                    lesson.description?.let { description ->
                        Text(
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = description,
                            typography = MaterialTheme.typography.labelMedium
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Chip(
                            label = DurationUtils.format(lesson.duration, true),
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            labelTypography = MaterialTheme.typography.titleSmall,
                            status = ChipStatusEnum.NORMAL
                        )

                        Spacer(Modifier.weight(1f))

                        if (lesson.isCompleted) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Green298
                            )
                        }
                    }
                }

                Spacer(Modifier.width(4.dp))

                Box(
                    Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}