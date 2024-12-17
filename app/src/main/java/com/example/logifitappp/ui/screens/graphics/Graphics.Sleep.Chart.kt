package com.example.logifitappp.ui.screens.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.core.graphics.SleepBarDataSet
import com.example.logifitappp.core.utils.DurationUtils
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.cards.InformationCard
import androidx.compose.material3.IconButton
import com.example.logifitappp.ui.components.graphics.SimpleHorizontalStackBar
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.logifitappp.core.wearebles.Wearable

@Composable
fun GraphicsSleepChart(
    dataSet: SleepBarDataSet,
    shift: ShiftModel?,
    navigation: NavHostController,
    wearable: Wearable?
) {
    val containsSleepData = !dataSet.empty

    Column(
        Modifier.clickable {
            wearable?.getAddress()?.let {
                navigation.navigate(MainRoutes.SleepDetail(it))
            }
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconText(
                icon = Icons.Default.NightsStay,
                iconColor = MaterialTheme.colorScheme.onSurface,
                iconSize = 24.dp,
                label = stringResource(id = R.string.sleep_detail_chart_title),
                labelTypography = MaterialTheme.typography.displayMedium
            )

            Spacer(Modifier.weight(1f))

            IconButton(
                onClick = { navigation.navigate(MainRoutes.SleepDataRecording) },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .size(24.dp)
            ) {
                Icon(
                    contentDescription = null,
                    imageVector = Icons.Filled.Add,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }
        }

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
                    label = if (!containsSleepData) stringResource(id = R.string.no_data) else DurationUtils.format(dataSet.amounts.totalSleepMinutes * 60),
                    status = if (!containsSleepData) ChipStatusEnum.NORMAL else ChipStatusEnum.SUCCESS
                )
            }
        ) {
            SimpleHorizontalStackBar(dataSet = dataSet)
        }
    }
}