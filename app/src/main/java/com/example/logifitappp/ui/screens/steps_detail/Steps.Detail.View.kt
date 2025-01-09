package com.example.logifitappp.ui.screens.steps_detail

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.InsertChartOutlined
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.StepsUtils
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.ui.components.graphics.VerticalBarChart
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.views.StepsDetailViewModel

@Composable
fun StepsDetailView(
    navigation: NavHostController,
    mac: String
) {
    val stepsDetailViewModel = hiltViewModel<StepsDetailViewModel, StepsDetailViewModel.StepsDetailViewModelFactory> {
        it.create(mac)
    }

    ScrollablePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.steps_detail_title)
            )
        }
    ) {
        item {
            StepsDetailHeader(stepsDetailViewModel)
            Spacer(Modifier.height(16.dp))

            VerticalBarChart(
                Modifier.fillMaxWidth().height(170.dp),
                dataSet = stepsDetailViewModel.state.stepsDataSet.self,
                yMax = stepsDetailViewModel.state.stepsDataSet.yMax
            )
        }

        if (!stepsDetailViewModel.state.stepsDataSet.empty) {
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
                    icon = Icons.Default.LocalFireDepartment,
                    label = stringResource(R.string.burned_calories),
                    suffixComponent = {
                        Chip(
                            label = "${StepsUtils.calculateKcal(stepsDetailViewModel.state.stepsDataSet.totalSteps)}kcal",
                            labelTypography = MaterialTheme.typography.titleSmall,
                            status = ChipStatusEnum.SUCCESS
                        )
                    }
                )

                Spacer(Modifier.height(8.dp))

                InformationCard(
                    icon = Icons.Default.Route,
                    label = stringResource(R.string.traveled_distance),
                    suffixComponent = {
                        Chip(
                            label = StepsUtils.formatDistance(StepsUtils.calculateDistance(stepsDetailViewModel.state.stepsDataSet.totalSteps)),
                            labelTypography = MaterialTheme.typography.titleSmall,
                            status = ChipStatusEnum.SUCCESS
                        )
                    }
                )
            }
        }
    }
}