package com.example.logifitappp.ui.screens.graphics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.core.graphics.StressDataSet
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.ui.components.graphics.VerticalBarChart

@Composable
fun GraphicsStressChart(
    dataset: StressDataSet,
    navigation: NavHostController,
    wearable: Wearable?
) {
    val containsStress = !dataset.empty

    Column(
        Modifier.clickable {
            wearable?.getAddress()?.let {
                navigation.navigate(MainRoutes.StressDetail(it))
            }
        }
    ) {
        IconText(
            icon = Icons.Default.EmojiEmotions,
            iconColor = MaterialTheme.colorScheme.onSurface,
            iconSize = 24.dp,
            label = stringResource(id = R.string.stress_detail_chart_title),
            labelTypography = MaterialTheme.typography.displayMedium
        )

        Spacer(Modifier.height(6.dp))

        InformationCard(
            icon = Icons.Default.QueryStats,
            label =  stringResource(id = R.string.stress_detail_chart_subtitle),
            suffixComponent = {
                Chip(
                    label = if (!containsStress) stringResource(id = R.string.no_data) else dataset.latestMeasuredStress.toString(),
                    status = if (!containsStress) ChipStatusEnum.NORMAL else ChipStatusEnum.SUCCESS
                )
            }
        ) {
            VerticalBarChart(
                Modifier.fillMaxWidth().height(80.dp),
                dataSet = dataset.self,
                yMax = dataset.yMax,
                yMin = dataset.yMin
            )
        }
    }
}