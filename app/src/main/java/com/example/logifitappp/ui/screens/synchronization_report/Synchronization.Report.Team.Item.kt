package com.example.logifitappp.ui.screens.synchronization_report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.viewmodel.states.SynchronizationReportItemType

@Composable
fun SynchronizationReportTeamItem(
    item: SynchronizationReportItemType
) {
    InformationCard(
        icon = Icons.Default.AccountCircle,
        label = item.label,
        suffixComponent = {
            if (item.sleepTime == null) {
                Chip(
                    label = item.condition,
                    status = ChipStatusEnum.CUSTOM(item.color, item.background)
                )
            } else {
                Chip(
                    label = item.sleepTime,
                    status = ChipStatusEnum.INFO
                )
            }
        }
    ) {
        Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            SynchronizationReportResumeItem(
                Modifier.weight(1f),
                label = stringResource(R.string.shift),
                value = item.shift,
                valueColor = if (item.shiftId == null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )

            VerticalDivider(
                color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f),
                thickness = 0.5.dp
            )

            SynchronizationReportResumeItem(
                Modifier.weight(1f),
                label = stringResource(R.string.group),
                value = item.group,
                valueColor = if (item.groupId == null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )

            if (item.fatigue != null) {
                VerticalDivider(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f),
                    thickness = 0.5.dp
                )

                Column(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        item.fatigue.calculateStatus().let {
                            Chip(
                                label = stringResource(it.label).uppercase(),
                                status = it.chipStatus
                            )
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        text = stringResource(R.string.fatigue),
                        typography = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}