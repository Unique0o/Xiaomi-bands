package com.example.logifitappp.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.data.models.FatigueModel
import com.example.logifitappp.data.models.SleepConditionModel
import com.example.logifitappp.enums.SleepProcessingStatusEnum
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.layouts.CardLayout

@Composable
fun SleepProcessingCard(
    modifier: Modifier = Modifier,
    drowsinessCondition: SleepConditionModel? = null,
    fatigue: FatigueModel? = null
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CardLayout(
            background = MaterialTheme.colorScheme.surface,
            modifier = Modifier.weight(1f)
        ) {
            SleepProcessingComponent(
                chipLabel = drowsinessCondition?.name,
                label = stringResource(id = R.string.drowsiness_label),
                sleepProcessingStatusEnum = drowsinessCondition?.calculateStatus() ?: SleepProcessingStatusEnum.PENDING
            )
        }

        Spacer(Modifier.width(8.dp))

        CardLayout(
            background = MaterialTheme.colorScheme.surface,
            modifier = Modifier.weight(1f)
        ) {
            SleepProcessingComponent(
                label = stringResource(id = R.string.fatigue_label),
                sleepProcessingStatusEnum = fatigue?.calculateStatus() ?: SleepProcessingStatusEnum.PENDING
            )
        }
    }
}

@Composable
fun SleepProcessingComponent(
    chipLabel: String? = null,
    label: String,
    sleepProcessingStatusEnum: SleepProcessingStatusEnum
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = label.uppercase(),
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(10.dp))

        Icon(
            contentDescription = null,
            imageVector = sleepProcessingStatusEnum.icon,
            tint = sleepProcessingStatusEnum.color
        )

        Spacer(Modifier.height(10.dp))

        Chip(
            label = chipLabel?.uppercase() ?: stringResource(id = sleepProcessingStatusEnum.label).uppercase(),
            status = sleepProcessingStatusEnum.chipStatus
        )
    }
}