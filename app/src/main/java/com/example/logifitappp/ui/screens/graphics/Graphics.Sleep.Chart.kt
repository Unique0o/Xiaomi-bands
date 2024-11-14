package com.example.logifitappp.ui.screens.graphics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.graphics.SleepBarDataSet
import com.example.logifitappp.core.utils.DurationUtils
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.ui.components.graphics.SimpleHorizontalStackBar

@Composable
fun GraphicsSleepChart(
    dataSet: SleepBarDataSet,
    shift: ShiftModel?
) {
    val containsSleepData = !dataSet.empty

    Column {
        IconText(
            icon = Icons.Default.NightsStay,
            iconColor = MaterialTheme.colorScheme.onSurface,
            iconSize = 24.dp,
            label = stringResource(id = R.string.sleep_detail_chart_title),
            labelTypography = MaterialTheme.typography.displayMedium
        )

        Spacer(Modifier.height(6.dp))

        InformationCard(
            icon = Icons.Default.Update,
            label = stringResource(
                id = R.string.sleep_detail_chart_subtitle,
                shift?.startTime ?: "NA",
                shift?.endTime ?: "NA",
            ),
            suffixComponent = {
                Chip(
                    label = if (!containsSleepData) stringResource(id = R.string.no_data) else DurationUtils.format(dataSet.totalTime),
                    status = if (!containsSleepData) ChipStatusEnum.NORMAL else ChipStatusEnum.SUCCESS
                )
            }
        ) {
            SimpleHorizontalStackBar(
                Modifier.fillMaxWidth().height(40.dp),
                dataSet = dataSet
            )
        }
    }
}