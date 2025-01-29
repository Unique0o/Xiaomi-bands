package com.example.logifitappp.ui.screens.synchronization_report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.MarkdownText
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
            if (item.fatigue == null) {
                Chip(
                    label = item.condition,
                    status = ChipStatusEnum.CUSTOM(item.color, item.background)
                )
            }
        }
    ) {
        MarkdownText(
            boldTextTypography = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            normalTextTypography = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            text = stringResource(R.string.shift_description, item.shift)
        )

        MarkdownText(
            boldTextTypography = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            normalTextTypography = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            text = stringResource(R.string.group_description, item.group)
        )

        if (item.fatigue != null) {
            Spacer(Modifier.height(8.dp))

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.drowsiness_label),
                        textAlign = TextAlign.Center,
                        typography = MaterialTheme.typography.titleLarge
                    )

                    Chip(
                        modifier = Modifier.fillMaxWidth(),
                        label = item.condition,
                        labelModifier = Modifier.fillMaxWidth(),
                        labelTextAlign = TextAlign.Center,
                        status = ChipStatusEnum.CUSTOM(item.color, item.background),
                        shouldItShowDotComponent = false
                    )
                }

                Spacer(Modifier.width(4.dp))

                Column(Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.fatigue_label),
                        textAlign = TextAlign.Center,
                        typography = MaterialTheme.typography.titleLarge
                    )

                    item.fatigue.calculateStatus().let {
                        Chip(
                            modifier = Modifier.fillMaxWidth(),
                            label = stringResource(it.label).uppercase(),
                            labelModifier = Modifier.fillMaxWidth(),
                            labelTextAlign = TextAlign.Center,
                            status = it.chipStatus,
                            shouldItShowDotComponent = false
                        )
                    }
                }
            }
        }
    }
}