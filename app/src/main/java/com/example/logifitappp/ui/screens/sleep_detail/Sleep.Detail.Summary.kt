package com.example.logifitappp.ui.screens.sleep_detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.analyzers.ActivityAmountList
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.core.utils.DurationUtils
import com.example.logifitappp.core.utils.MathUtils
import com.example.logifitappp.core.wearebles.WearableActivityTypeEnum
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
        Row(Modifier.fillMaxWidth()) {
            SleepDetailSummaryItem(
                Modifier.weight(1f),
                backgroundColor = WearableActivityTypeEnum.DEEP_SLEEP.color,
                percentage = MathUtils.percentage(amounts.totalDeepSleepMinutes, amounts.totalSleepMinutes),
                title = stringResource(R.string.deep_sleep),
                summary = DurationUtils.format(amounts.totalDeepSleepMinutes * 60)
            )

            Spacer(Modifier.width(4.dp))

            SleepDetailSummaryItem(
                Modifier.weight(1f),
                backgroundColor = WearableActivityTypeEnum.LIGHT_SLEEP.color,
                percentage = MathUtils.percentage(amounts.totalLightSleepMinutes, amounts.totalSleepMinutes),
                title = stringResource(R.string.light_sleep),
                summary = DurationUtils.format(amounts.totalLightSleepMinutes * 60)
            )
        }

        if (supportsRemSleep == true) {
            Spacer(Modifier.height(6.dp))

            SleepDetailSummaryItem(
                backgroundColor = WearableActivityTypeEnum.REM_SLEEP.color,
                percentage = MathUtils.percentage(amounts.totalRemSleepMinutes, amounts.totalSleepMinutes),
                title = stringResource(R.string.rem_sleep),
                summary = DurationUtils.format(amounts.totalRemSleepMinutes * 60)
            )
        }

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