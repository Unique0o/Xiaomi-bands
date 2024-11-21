package com.example.logifitappp.ui.screens.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
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

@Composable
fun GraphicsSleepChart(
    dataSet: SleepBarDataSet,
    shift: ShiftModel?,
    navigation: NavHostController
) {
    val containsSleepData = !dataSet.empty

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconText(
                icon = Icons.Default.NightsStay,
                iconColor = MaterialTheme.colorScheme.onSurface,
                iconSize = 24.dp,
                label = stringResource(id = R.string.sleep_detail_chart_title),
                labelTypography = MaterialTheme.typography.displayMedium
            )

            IconButton(
                onClick ={navigation.navigate(MainRoutes.AddSleepData)},
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .size(20.dp)
            ) {
                Icon(
                    contentDescription = null,
                    imageVector = Icons.Filled.Add,
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