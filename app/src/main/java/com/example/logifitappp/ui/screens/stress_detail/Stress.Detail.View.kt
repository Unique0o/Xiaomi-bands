package com.example.logifitappp.ui.screens.stress_detail

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.InsertChartOutlined
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.ui.components.graphics.VerticalBarChart
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.views.StressDetailViewModel

@Composable
fun StressDetailView(
    navigation: NavHostController,
    mac: String
) {
    val stressDetailViewModel = hiltViewModel<StressDetailViewModel, StressDetailViewModel.StressDetailViewModelFactory> {
        it.create(mac)
    }

    ScrollablePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.stress_detail_title)
            )
        }
    ) {
        item {
            StressDetailHeader(stressDetailViewModel)
            Spacer(Modifier.height(16.dp))

            VerticalBarChart(
                Modifier.fillMaxWidth().height(170.dp),
                dataSet = stressDetailViewModel.state.stressDataSet.self,
                yMax = stressDetailViewModel.state.stressDataSet.yMax,
                yMin = stressDetailViewModel.state.stressDataSet.yMin
            )
        }

        if (!stressDetailViewModel.state.stressDataSet.empty) {
            item {
                Spacer(Modifier.height(16.dp))

                IconText(
                    icon = Icons.Default.InsertChartOutlined,
                    iconColor = MaterialTheme.colorScheme.onSurface,
                    iconSize = 24.dp,
                    label = stringResource(R.string.summary),
                    labelTypography = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                Spacer(Modifier.height(8.dp))

                InformationCard(
                    icon = Icons.Default.AvTimer,
                    label = stringResource(R.string.average_stress),
                    suffixComponent = {
                        Chip(
                            label = stressDetailViewModel.state.stressDataSet.averageStress.toString(),
                            labelTypography = MaterialTheme.typography.titleSmall,
                            status = ChipStatusEnum.SUCCESS
                        )
                    }
                )

                Spacer(Modifier.height(8.dp))

                InformationCard(
                    icon = Icons.Default.QueryStats,
                    label = stringResource(R.string.last_recorded_measurement),
                    suffixComponent = {
                        Chip(
                            label = stressDetailViewModel.state.stressDataSet.latestMeasuredStress.toString(),
                            labelTypography = MaterialTheme.typography.titleSmall,
                            status = ChipStatusEnum.SUCCESS
                        )
                    }
                )
            }
        }
    }
}