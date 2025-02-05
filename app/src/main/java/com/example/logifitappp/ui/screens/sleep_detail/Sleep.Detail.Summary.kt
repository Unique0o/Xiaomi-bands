package com.example.logifitappp.ui.screens.sleep_detail

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.analyzers.ActivityAmountList
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.enums.WearableActivityTypeEnum
import com.example.logifitappp.ui.components.SleepInformationGroup
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.ui.components.graphics.TimedProgressBar

@Composable
fun SleepDetailSummary(
    amounts: ActivityAmountList,
    supportsRemSleep: Boolean?
) {
    InformationCard(
        icon = Icons.Default.Update,
        label = stringResource(id = R.string.sleep_information)
    ) {
        SleepInformationGroup(
            deepSleepMinutes = amounts.totalDeepSleepMinutes,
            lightSleepMinutes = amounts.totalLightSleepMinutes,
            remSleepMinutes = amounts.totalRemSleepMinutes,
            supportsRemSleep = supportsRemSleep,
            totalSleepMinutes = amounts.totalSleepMinutes
        )

        amounts.getSleeps(0).forEach { sleep ->
            Spacer(Modifier.height(16.dp))

            TimedProgressBar(
                colors = listOf(
                    WearableActivityTypeEnum.DEEP_SLEEP.color,
                    WearableActivityTypeEnum.REM_SLEEP.color,
                    WearableActivityTypeEnum.LIGHT_SLEEP.color
                ),
                duration = sleep.totalSleepSeconds,
                endAt = DateTimeUtils.parse(sleep.endAt, "yyyy-MM-dd HH:mm:ss", "HH:mm"),
                progressValues = listOf(
                    sleep.deepSleepSeconds / sleep.totalSleepSeconds.toFloat(),
                    sleep.remSleepSeconds / sleep.totalSleepSeconds.toFloat(),
                    sleep.lightSleepSeconds / sleep.totalSleepSeconds.toFloat()
                ),
                startAt = DateTimeUtils.parse(sleep.startAt, "yyyy-MM-dd HH:mm:ss", "HH:mm")
            )
        }
    }
}