package com.example.logifitappp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.DurationUtils
import com.example.logifitappp.core.utils.MathUtils
import com.example.logifitappp.enums.WearableActivityTypeEnum
import com.example.logifitappp.ui.screens.sleep_detail.SleepDetailSummaryItem

@Composable
fun SleepInformationGroup(
    deepSleepMinutes: Long,
    lightSleepMinutes: Long,
    remSleepMinutes: Long,
    totalSleepMinutes: Long,
    supportsRemSleep: Boolean?
) {
    Row(Modifier.fillMaxWidth()) {
        SleepDetailSummaryItem(
            Modifier.weight(1f),
            backgroundColor = WearableActivityTypeEnum.DEEP_SLEEP.color,
            percentage = MathUtils.percentage(deepSleepMinutes, totalSleepMinutes),
            title = stringResource(R.string.deep_sleep),
            summary = DurationUtils.format(deepSleepMinutes * 60)
        )

        Spacer(Modifier.width(4.dp))

        SleepDetailSummaryItem(
            Modifier.weight(1f),
            backgroundColor = WearableActivityTypeEnum.LIGHT_SLEEP.color,
            percentage = MathUtils.percentage(lightSleepMinutes, totalSleepMinutes),
            title = stringResource(R.string.light_sleep),
            summary = DurationUtils.format(lightSleepMinutes * 60)
        )
    }

    if (supportsRemSleep == true) {
        Spacer(Modifier.height(6.dp))

        SleepDetailSummaryItem(
            backgroundColor = WearableActivityTypeEnum.REM_SLEEP.color,
            percentage = MathUtils.percentage(remSleepMinutes, totalSleepMinutes),
            title = stringResource(R.string.rem_sleep),
            summary = DurationUtils.format(remSleepMinutes * 60)
        )
    }
}