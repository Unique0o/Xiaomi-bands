package com.example.logifitappp.ui.screens.sleep_detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.InsertChartOutlined
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.core.utils.DurationUtils
import com.example.logifitappp.core.utils.MathUtils
import com.example.logifitappp.core.wearebles.WearableActivityTypeEnum
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.viewmodel.views.SleepDetailViewModel

@Composable
fun SleepDetailSummary(
    sleepDetailViewModel: SleepDetailViewModel
) {
    val amounts = sleepDetailViewModel.state.sleepDataSet.amounts

    Box (Modifier.padding(horizontal = 12.dp)) {
        IconText(
            icon = Icons.Default.InsertChartOutlined,
            iconColor = MaterialTheme.colorScheme.onSurface,
            iconSize = 24.dp,
            label = stringResource(R.string.summary),
            labelTypography = MaterialTheme.typography.displayMedium,
        )
    }

    Spacer(Modifier.height(8.dp))

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

        if (sleepDetailViewModel.state.wearable?.getWearableCoordinator()?.supportsRemSleep() == true) {
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

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier.width(64.dp),
                        text = DurationUtils.format(sleep.totalSleepSeconds),
                        typography = MaterialTheme.typography.bodySmall
                    )

                    Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))) {
                        Canvas(Modifier.fillMaxWidth().height(8.dp)) {
                            val totalWidth = size.width
                            var startX = 0f

                            arrayListOf(
                                Pair(sleep.deepSleepSeconds, WearableActivityTypeEnum.DEEP_SLEEP.color),
                                Pair(sleep.remSleepSeconds, WearableActivityTypeEnum.REM_SLEEP.color),
                                Pair(sleep.lightSleepSeconds, WearableActivityTypeEnum.LIGHT_SLEEP.color)
                            ). forEach { pair ->
                                val segmentWidth = totalWidth * (pair.first / sleep.totalSleepSeconds.toFloat())

                                drawRect(
                                    color = pair.second,
                                    size = Size(width = segmentWidth, height = size.height),
                                    topLeft = Offset(x = startX, y = 0f),
                                )

                                startX += segmentWidth
                            }
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(Modifier.width(64.dp))

                    Text(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        text = DateTimeUtils.parse(sleep.startAt, "yyyy-MM-dd HH:mm:ss", "HH:mm"),
                        typography = MaterialTheme.typography.labelSmall
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        text = DateTimeUtils.parse(sleep.endAt, "yyyy-MM-dd HH:mm:ss", "HH:mm"),
                        typography = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}