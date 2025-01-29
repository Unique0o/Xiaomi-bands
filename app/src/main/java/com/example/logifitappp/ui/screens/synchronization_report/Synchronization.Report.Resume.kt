package com.example.logifitappp.ui.screens.synchronization_report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PieChartOutline
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.viewmodel.states.SynchronizationReportState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SynchronizationReportResume(
    state: SynchronizationReportState
) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        InformationCard(
            icon = Icons.Default.PieChartOutline,
            label = stringResource(R.string.statistics_title)
        ) {
            FlowRow(Modifier.fillMaxWidth(), maxItemsInEachRow = 4) {
                Row(Modifier.weight(1f).height(IntrinsicSize.Min), horizontalArrangement = Arrangement.Center) {
                    SynchronizationReportResumeItem(
                        label = stringResource(R.string.total_label),
                        value = state.total.toString()
                    )
                }

                state.conditions.forEachIndexed { index, condition ->
                    Row(Modifier.weight(1f).height(IntrinsicSize.Min), verticalAlignment = Alignment.CenterVertically) {
                        if ((index + 1) % 4 != 0) {
                            VerticalDivider(
                                color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f),
                                thickness = 0.5.dp
                            )
                        }

                        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            SynchronizationReportResumeItem(
                                label = condition.label,
                                value = condition.value.toString()
                            )
                        }
                    }
                }
            }
        }
    }
}