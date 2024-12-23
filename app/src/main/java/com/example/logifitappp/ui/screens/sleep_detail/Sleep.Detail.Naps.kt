package com.example.logifitappp.ui.screens.sleep_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.analyzers.ActivityAmountList
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.core.utils.DurationUtils
import com.example.logifitappp.core.wearebles.WearableActivityTypeEnum
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.ProgressBar
import com.example.logifitappp.ui.components.SleepInformationGroup
import com.example.logifitappp.ui.components.cards.InformationCard

@Composable
fun SleepDetailNaps(
    naps: ActivityAmountList,
    supportsRemSleep: Boolean?
) {
    val sleeps = naps.getSleeps(0)

    if (sleeps.isEmpty()) return

    Box (Modifier.padding(horizontal = 12.dp)) {
        IconText(
            icon = Icons.Default.Bed,
            iconColor = MaterialTheme.colorScheme.onSurface,
            iconSize = 24.dp,
            label = stringResource(R.string.naps),
            labelTypography = MaterialTheme.typography.displayMedium,
        )
    }

    sleeps.forEach {
        Spacer(Modifier.height(8.dp))

        InformationCard(
            icon = Icons.Default.Timelapse,
            label = "${DateTimeUtils.parse(it.startAt, "yyyy-MM-dd HH:mm:ss", "HH:mm")} - ${DateTimeUtils.parse(it.endAt, "yyyy-MM-dd HH:mm:ss", "HH:mm")}",
            suffixComponent = {
                Chip(
                    label = DurationUtils.format(it.totalSleepSeconds),
                    labelTypography = MaterialTheme.typography.titleSmall,
                    status = ChipStatusEnum.SUCCESS
                )
            }
        ) {
            ProgressBar(
                colors = listOf(
                    WearableActivityTypeEnum.DEEP_SLEEP.color,
                    WearableActivityTypeEnum.REM_SLEEP.color,
                    WearableActivityTypeEnum.LIGHT_SLEEP.color
                ),
                modifier = Modifier.fillMaxWidth(),
                progressValues = listOf(
                    it.deepSleepSeconds / it.totalSleepSeconds.toFloat(),
                    it.remSleepSeconds / it.totalSleepSeconds.toFloat(),
                    it.lightSleepSeconds / it.totalSleepSeconds.toFloat()
                )
            )

            Spacer(Modifier.height(12.dp))

            SleepInformationGroup(
                deepSleepMinutes = it.deepSleepSeconds / 60,
                lightSleepMinutes = it.lightSleepSeconds / 60,
                remSleepMinutes = it.remSleepSeconds / 60,
                supportsRemSleep = supportsRemSleep,
                totalSleepMinutes = it.totalSleepSeconds / 60
            )
        }
    }
}