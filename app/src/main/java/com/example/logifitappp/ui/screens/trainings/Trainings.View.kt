package com.example.logifitappp.ui.screens.trainings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.ProgressiveImage
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.LoaderPage
import com.example.logifitappp.ui.components.pages.NoInternetPage
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.views.TrainingsViewModel

@Composable
fun TrainingsView(
    navigation: NavHostController
) {
    val trainingsViewModel: TrainingsViewModel = hiltViewModel()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        trainingsViewModel.updateProgress()
    }

    if (trainingsViewModel.state.isLoading) {
        LoaderPage()
        return
    }

    if (trainingsViewModel.state.hasFetchTrainingsFailed) {
        NoInternetPage { trainingsViewModel.fetchTrainings() }
        return
    }

    ScrollablePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.trainings_title)
            )
        }
    ) {
        items(trainingsViewModel.trainings) {
            Row(
                Modifier
                    .height(100.dp)
                    .clickable { navigation.navigate(MainRoutes.TrainingsDetail(it.id)) }
            ) {
                ProgressiveImage(
                    modifier = Modifier.width(130.dp)
                        .fillMaxHeight()
                        .defaultMinSize(minHeight = 70.dp),
                    url = it.image
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
                        text = it.name,
                        typography = MaterialTheme.typography.displayMedium
                    )

                    it.description?.let { description ->
                        Text(
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = description,
                            typography = MaterialTheme.typography.labelMedium
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    it.getStatusPair()?.let { pair ->
                        Chip(
                            label = stringResource(pair.first),
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            labelTypography = MaterialTheme.typography.titleSmall,
                            status = pair.second
                        )
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