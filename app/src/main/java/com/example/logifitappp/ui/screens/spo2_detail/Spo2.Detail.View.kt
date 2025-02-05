package com.example.logifitappp.ui.screens.spo2_detail

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.material.icons.filled.InsertChartOutlined
import androidx.compose.material.icons.filled.WifiTethering
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
import com.example.logifitappp.viewmodel.views.Spo2DetailViewModel

@Composable
fun Spo2DetailView(
    navigation: NavHostController,
    mac: String
) {
    val spo2DetailViewModel = hiltViewModel<Spo2DetailViewModel, Spo2DetailViewModel.Spo2DetailViewModelFactory>{
        it.create(mac)
    }

    ScrollablePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.spo2_detail_title)
            )
        }
    ) {
        item {
            Spo2DetailHeader(spo2DetailViewModel)
            Spacer(Modifier.height(16.dp))

            VerticalBarChart(
                Modifier.fillMaxWidth().height(170.dp),
                dataSet = spo2DetailViewModel.state.spo2DataSet.self,
                yMax = spo2DetailViewModel.state.spo2DataSet.yMax,
                yMin = spo2DetailViewModel.state.spo2DataSet.yMin
            )
        }

        if (!spo2DetailViewModel.state.spo2DataSet.empty) {
            item {
                Spacer(Modifier.height(8.dp))
                Spo2DetailLegend()
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
                    icon = Icons.Default.WifiTethering,
                    label = stringResource(R.string.average_spo2),
                    suffixComponent = {
                        Chip(
                            label = "${spo2DetailViewModel.state.spo2DataSet.averageSpo2}%",
                            labelTypography = MaterialTheme.typography.titleSmall,
                            status = ChipStatusEnum.SUCCESS
                        )
                    }
                )

                Spacer(Modifier.height(8.dp))

                InformationCard(
                    icon = Icons.Default.BubbleChart,
                    label = stringResource(R.string.last_recorded_measurement),
                    suffixComponent = {
                        Chip(
                            label = "${spo2DetailViewModel.state.spo2DataSet.latestMeasuredSpo2}%",
                            labelTypography = MaterialTheme.typography.titleSmall,
                            status = ChipStatusEnum.SUCCESS
                        )
                    }
                )
            }
        }
    }
}