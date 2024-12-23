package com.example.logifitappp.ui.screens.sleep_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.InsertChartOutlined
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Snooze
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.enums.SleepProcessingStatusEnum
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.ui.components.graphics.SimpleHorizontalStackBar
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.SleepDetailViewModel

@Composable
fun SleepDetailView(
    appViewModel: AppViewModel,
    navigation: NavHostController,
    mac: String
) {
    val sleepDetailViewModel = hiltViewModel<SleepDetailViewModel, SleepDetailViewModel.SleepDetailViewModelFactory>{
        it.create(mac, appViewModel.user)
    }

    ScrollablePage(
        contentPadding = PaddingValues(0.dp),
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.sleep_detail_title)
            )
        }
    ) {
        item {
            SleepDetailHeader(sleepDetailViewModel)
            Spacer(Modifier.height(16.dp))
            SimpleHorizontalStackBar(dataSet = sleepDetailViewModel.state.sleepDataSet)
        }

        if (!sleepDetailViewModel.state.sleepDataSet.empty) {
            item {
                Column(Modifier.padding(vertical = 16.dp, horizontal = 12.dp)) {
                    Box (Modifier.padding(horizontal = 12.dp)) {
                        IconText(
                            icon = Icons.Default.InsertChartOutlined,
                            iconColor = MaterialTheme.colorScheme.onSurface,
                            iconSize = 24.dp,
                            label = stringResource(R.string.summary),
                            labelTypography = MaterialTheme.typography.displayMedium,
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    SleepDetailSummary(
                        amounts = sleepDetailViewModel.state.sleepDataSet.amounts,
                        supportsRemSleep = sleepDetailViewModel.state.wearable?.getWearableCoordinator()?.supportsRemSleep()
                    )

                    Spacer(Modifier.height(8.dp))

                    SleepDetailAwakenings(amounts = sleepDetailViewModel.state.sleepDataSet.amounts)

                    Spacer(Modifier.height(8.dp))

                    InformationCard(
                        icon = Icons.Default.NightsStay,
                        label = stringResource(R.string.sleep_condition),
                        suffixComponent = {
                            (sleepDetailViewModel.state.sleepCondition?.calculateStatus() ?: SleepProcessingStatusEnum.PENDING).let {
                                Chip(
                                    label = stringResource(id = it.label).uppercase(),
                                    labelTypography = MaterialTheme.typography.titleSmall,
                                    status = it.chipStatus
                                )
                            }
                        }
                    )

                    Spacer(Modifier.height(8.dp))

                    InformationCard(
                        icon = Icons.Default.Snooze,
                        label = stringResource(R.string.fatigue),
                        suffixComponent = {
                            (sleepDetailViewModel.state.fatigue?.calculateStatus() ?: SleepProcessingStatusEnum.PENDING).let {
                                Chip(
                                    label = stringResource(id = it.label).uppercase(),
                                    labelTypography = MaterialTheme.typography.titleSmall,
                                    status = it.chipStatus
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}