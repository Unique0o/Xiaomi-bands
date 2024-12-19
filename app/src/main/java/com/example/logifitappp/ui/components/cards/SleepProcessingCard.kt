package com.example.logifitappp.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.data.models.DrowsinessModel
import com.example.logifitappp.data.models.FatigueModel
import com.example.logifitappp.data.models.SleepConditionModel
import com.example.logifitappp.data.models.TenantModel
import com.example.logifitappp.enums.SleepProcessingStatusEnum
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.layouts.CardLayout
import com.example.logifitappp.ui.components.modals.DrowsinessDetailModal
import com.example.logifitappp.ui.components.modals.FatigueDetailModal

@Composable
fun SleepProcessingCard(
    modifier: Modifier = Modifier,
    drowsiness: DrowsinessModel?,
    drowsinessCondition: SleepConditionModel?,
    fatigue: FatigueModel?,
    tenant: TenantModel?
) {
    var isDrowsinessDetailModalVisible by remember { mutableStateOf(false) }
    var isFatigueDetailModalVisible by remember { mutableStateOf(false) }

    DrowsinessDetailModal(
        onClose = { isDrowsinessDetailModalVisible = false },
        onDismissRequest = { isDrowsinessDetailModalVisible = false },
        drowsiness = drowsiness,
        drowsinessCondition = drowsinessCondition,
        tenant = tenant,
        visible = isDrowsinessDetailModalVisible
    )

    FatigueDetailModal(
        onClose = { isFatigueDetailModalVisible = false },
        onDismissRequest = { isFatigueDetailModalVisible = false },
        fatigue = fatigue,
        visible = isFatigueDetailModalVisible
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CardLayout(
            background = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .weight(1f)
                .clickable {
                    if (drowsiness != null) isDrowsinessDetailModalVisible = true
                }
        ) {
            SleepProcessingComponent(
                chipLabel = drowsinessCondition?.name,
                label = stringResource(id = R.string.drowsiness_label).uppercase(),
                sleepProcessingStatusEnum = drowsinessCondition?.calculateStatus() ?: SleepProcessingStatusEnum.PENDING
            )
        }

        Spacer(Modifier.width(8.dp))

        CardLayout(
            background = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .weight(1f)
                .clickable {
                    if (fatigue != null && fatigue.totalSleepSeconds > 0L) isFatigueDetailModalVisible = true
                }
        ) {
            SleepProcessingComponent(
                label = stringResource(id = R.string.fatigue_label).uppercase(),
                sleepProcessingStatusEnum =  if (fatigue != null && fatigue.totalSleepSeconds > 0L) fatigue.calculateStatus() else SleepProcessingStatusEnum.PENDING
            )
        }
    }
}

@Composable
fun SleepProcessingComponent(
    chipLabel: String? = null,
    iconSize: Dp = 10.dp,
    label: String,
    sleepProcessingStatusEnum: SleepProcessingStatusEnum
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = label,
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(10.dp))

        Icon(
            contentDescription = null,
            imageVector = sleepProcessingStatusEnum.icon,
            modifier = Modifier.size(iconSize),
            tint = sleepProcessingStatusEnum.color
        )

        Spacer(Modifier.height(10.dp))

        Chip(
            label = chipLabel?.uppercase() ?: stringResource(id = sleepProcessingStatusEnum.label).uppercase(),
            labelTypography = MaterialTheme.typography.titleSmall,
            status = sleepProcessingStatusEnum.chipStatus
        )
    }
}