package com.example.logifitappp.ui.screens.heart_rate_detail

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.InsertChartOutlined
import androidx.compose.material.icons.filled.MonitorHeart
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
import com.example.logifitappp.viewmodel.views.HeartRateDetailViewModel

@Composable
fun HeartRateDetailView(
    navigation: NavHostController,
    mac: String
) {
    val heartRateDetailViewModel = hiltViewModel<HeartRateDetailViewModel, HeartRateDetailViewModel.HeartRateDetailViewModelFactory> {
        it.create(mac)
    }

    ScrollablePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.heart_rate_detail_title)
            )
        }
    ) {
        item {
            HeartRateDetailHeader(heartRateDetailViewModel)
            Spacer(Modifier.height(16.dp))

            VerticalBarChart(
                Modifier.fillMaxWidth().height(170.dp),
                dataSet = heartRateDetailViewModel.state.heartRateDataSet.self,
                yMax = heartRateDetailViewModel.state.heartRateDataSet.yMax
            )
        }

        if (!heartRateDetailViewModel.state.heartRateDataSet.empty) {
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
                    icon = Icons.Default.MonitorHeart,
                    label = stringResource(R.string.average_heart_rate),
                    suffixComponent = {
                        Chip(
                            label = "${heartRateDetailViewModel.state.heartRateDataSet.averageHeartRate} LPM",
                            labelTypography = MaterialTheme.typography.titleSmall,
                            status = ChipStatusEnum.SUCCESS
                        )
                    }
                )

                Spacer(Modifier.height(8.dp))

                InformationCard(
                    icon = Icons.Default.HeartBroken,
                    label = stringResource(R.string.last_recorded_measurement),
                    suffixComponent = {
                        Chip(
                            label = "${heartRateDetailViewModel.state.heartRateDataSet.latestMeasuredHeartRate} LPM",
                            labelTypography = MaterialTheme.typography.titleSmall,
                            status = ChipStatusEnum.SUCCESS
                        )
                    }
                )
            }
        }
    }
}