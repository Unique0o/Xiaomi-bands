package com.example.logifitappp.ui.screens.graphics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.core.graphics.StepsDataSet
import com.example.logifitappp.core.utils.StepsUtils
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.ui.components.graphics.VerticalBarChart

@Composable
fun GraphicsStepsChart(
    dataset: StepsDataSet,
    navigation: NavHostController,
    wearable: Wearable?
) {
    val containsSteps = !dataset.empty

    Column(
        Modifier.clickable {
            wearable?.getAddress()?.let {
                navigation.navigate(MainRoutes.StepsDetail(it))
            }
        }
    ) {
        IconText(
            icon = Icons.AutoMirrored.Filled.DirectionsRun,
            iconColor = MaterialTheme.colorScheme.onSurface,
            iconSize = 24.dp,
            label = stringResource(id = R.string.steps_detail_chart_title),
            labelTypography = MaterialTheme.typography.displayMedium
        )

        Spacer(Modifier.height(6.dp))

        InformationCard(
            icon = Icons.Default.LocalFireDepartment,
            label = "${StepsUtils.calculateKcal(dataset.totalSteps)}kcal",
            suffixComponent = {
                Chip(
                    label = if (!containsSteps) stringResource(id = R.string.no_data) else stringResource(
                        id = R.string.steps_detail_chart_chip_message,
                        dataset.totalSteps
                    ),
                    status = if (!containsSteps) ChipStatusEnum.NORMAL else ChipStatusEnum.SUCCESS
                )
            }
        ) {
            VerticalBarChart(
                Modifier.fillMaxWidth().height(80.dp),
                dataSet = dataset.self,
                yMax = dataset.yMax
            )
        }
    }
}