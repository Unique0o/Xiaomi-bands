package com.example.logifitappp.ui.screens.graphics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.graphics.HeartRateDataSet
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.ui.components.graphics.VerticalBar

@Composable
fun GraphicsHeartRateChart(
    dataset: HeartRateDataSet
) {
    val containsHeartRate = !dataset.empty

    Column {
        IconText(
            icon = Icons.Outlined.MonitorHeart,
            iconColor = MaterialTheme.colorScheme.onSurface,
            iconSize = 24.dp,
            label = stringResource(id = R.string.heart_rate_detail_chart_title),
            labelTypography = MaterialTheme.typography.displayMedium
        )

        Spacer(Modifier.height(6.dp))

        InformationCard(
            icon = Icons.Default.HeartBroken,
            label =  stringResource(id = R.string.heart_rate_detail_chart_subtitle),
            suffixComponent = {
                Chip(
                    label = if (!containsHeartRate) stringResource(id = R.string.no_data) else "${dataset.latestMeasuredHeartRate} LPM",
                    status = if (!containsHeartRate) ChipStatusEnum.NORMAL else ChipStatusEnum.SUCCESS
                )
            }
        ) {
            VerticalBar(
                Modifier.fillMaxWidth().height(80.dp),
                dataSet = dataset.self,
                yMax = dataset.yMax
            )
        }
    }
}