package com.example.logifitappp.ui.screens.sleep_detail

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.analyzers.ActivityAmountList
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.core.utils.DurationUtils
import com.example.logifitappp.enums.WearableActivityTypeEnum
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.ui.components.graphics.TimedProgressBar

@Composable
fun SleepDetailAwakenings(
    amounts: ActivityAmountList
) {
    val awakenings = amounts.getAwakenings()

    InformationCard(
        icon = Icons.Default.Lightbulb,
        label = stringResource(R.string.awaken),
        suffixComponent = {
            Chip(
                label = DurationUtils.format(awakenings.sumOf { it.duration }),
                status = ChipStatusEnum.SUCCESS
            )
        }
    ) {
        awakenings.forEachIndexed { index, awakening ->
            if (index != 0) Spacer(Modifier.height(16.dp))

            TimedProgressBar(
                colors = listOf(WearableActivityTypeEnum.ACTIVITY.color),
                duration = awakening.duration,
                endAt = DateTimeUtils.parse(awakening.endAt, "yyyy-MM-dd HH:mm:ss", "HH:mm"),
                progressValues = listOf(1f),
                startAt = DateTimeUtils.parse(awakening.startAt, "yyyy-MM-dd HH:mm:ss", "HH:mm")
            )
        }
    }
}