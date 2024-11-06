package com.example.logifitappp.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.ui.components.cards.InformationOptionCard
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.viewmodel.views.HomeViewModel

@Composable
fun HomeDrowsinessTest(
    homeViewModel: HomeViewModel,
    navigation: NavHostController,
) {
    Column {
        InformationOptionCard(
            buttonIcon = Icons.Filled.Add,
            icon = Icons.Outlined.FormatListNumbered,
            modifier = Modifier.padding(horizontal = 6.dp),
            onClick = { navigation.navigate(MainRoutes.WearableDetection) },
            paragraph = stringResource(id = R.string.drowsiness_evaluation_message),
            title =  stringResource(id = R.string.my_drowsiness_tests)
        )

        homeViewModel.evaluations.map { evaluation ->
            Spacer(Modifier.height(16.dp))

            InformationCard(
                icon = Icons.Default.Edit,
                label = evaluation.title,
                suffixComponent = {
                    evaluation.calculateStatus().let {
                        Chip(
                            label = stringResource(id = it.label),
                            status = it.chipStatus
                        )
                    }
                }
            ) {
                Column(Modifier.padding(start = 28.dp)) {
                    Text(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        text = stringResource(
                            id = R.string.drowsiness_evaluation_date_label,
                            DateTimeUtils.parse(evaluation.createdAt, "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy")
                        ),
                        typography = MaterialTheme.typography.labelMedium
                    )

                    Spacer(Modifier.height(24.dp))

                    IconButton(
                        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                        horizontalPadding = 10.dp,
                        icon = Icons.Default.Share,
                        iconSize = 10.dp,
                        modifier = Modifier.height(24.dp),
                        text = stringResource(id = R.string.share),
                        onClick = { homeViewModel.shareEvaluation(evaluation) },
                        verticalPadding = 0.dp,
                    )
                }
            }
        }
    }
}