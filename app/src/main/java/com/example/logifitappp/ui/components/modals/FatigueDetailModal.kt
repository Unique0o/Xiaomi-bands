package com.example.logifitappp.ui.components.modals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.data.models.FatigueModel
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.enums.SleepProcessingStatusEnum
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.cards.SleepParameterCard
import com.example.logifitappp.ui.components.layouts.ModalLayout

@Composable
fun FatigueDetailModal(
    onClose: () -> Unit,
    onDismissRequest: () -> Unit,
    fatigue: FatigueModel?,
    visible: Boolean
) {
    ModalLayout(
        onClose = onClose,
        onDismissRequest = onDismissRequest,
        visible = visible
    ) {
        val condition = fatigue?.calculateStatus() ?: SleepProcessingStatusEnum.PENDING

        Text(
            color = MaterialTheme.colorScheme.primary,
            text = stringResource(id = R.string.fatigue_detail_modal_title),
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.displayMedium
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                color = MaterialTheme.colorScheme.surfaceTint,
                text = stringResource(R.string.fatigue_detail_modal_subtitle),
                typography = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.width(8.dp))

            Chip(
                label = stringResource(id = condition.label).uppercase(),
                labelTypography = MaterialTheme.typography.titleSmall,
                status = condition.chipStatus
            )
        }

        Spacer(Modifier.height(16.dp))

        SleepParameterCard(
            image = R.drawable.ic_sleep_parameter,
            label = stringResource(R.string.sleep_time)
        ) {
            Chip(
                label = stringResource(if (fatigue?.withLittleSleep == true) R.string.less_than_6_hours else R.string.greater_than_6_hours),
                labelTypography = MaterialTheme.typography.titleSmall,
                status = if (fatigue?.withLittleSleep == true) ChipStatusEnum.DANGER else ChipStatusEnum.SUCCESS
            )
        }

        Spacer(Modifier.height(16.dp))

        SleepParameterCard(
            image = R.drawable.ic_awaken_overcome_parameter,
            label = stringResource(R.string.awaken)
        ) {
            Chip(
                label = stringResource(if (fatigue?.withAwakeningOvercome == true) R.string.greater_than_20_minutes else R.string.less_than_20_minutes),
                labelTypography = MaterialTheme.typography.titleSmall,
                status = if (fatigue?.withAwakeningOvercome == true) ChipStatusEnum.DANGER else ChipStatusEnum.SUCCESS
            )
        }

        Spacer(Modifier.height(16.dp))

        SleepParameterCard(
            image = R.drawable.ic_heart_rate_parameter,
            label = stringResource(R.string.hear_rate)
        ) {
            Chip(
                label = stringResource(if (fatigue?.withHypertension == true) R.string.less_than_40_or_greater_than_100 else R.string.between_40_and_100),
                labelTypography = MaterialTheme.typography.titleSmall,
                status = if (fatigue?.withHypertension == true) ChipStatusEnum.DANGER else ChipStatusEnum.SUCCESS
            )
        }

        Spacer(Modifier.height(16.dp))

        SleepParameterCard(
            image = R.drawable.ic_rem_sleep_parameter,
            label = stringResource(R.string.rem_sleep)
        ) {
            Chip(
                label = stringResource(if (fatigue?.withLittleReemSleep == true) R.string.less_than_15_percentage else R.string.greater_than_15_percentage),
                labelTypography = MaterialTheme.typography.titleSmall,
                status = if (fatigue?.withLittleReemSleep == true) ChipStatusEnum.DANGER else ChipStatusEnum.SUCCESS
            )
        }

        Spacer(Modifier.height(16.dp))

        SleepParameterCard(
            image = R.drawable.ic_total_awaken_time_parameter,
            label = stringResource(R.string.total_awake_time)
        ) {
            Chip(
                label = stringResource(if (fatigue?.withLongAwake == true) R.string.greater_than_1_hour else R.string.less_than_1_hour),
                labelTypography = MaterialTheme.typography.titleSmall,
                status = if (fatigue?.withLongAwake == true) ChipStatusEnum.DANGER else ChipStatusEnum.SUCCESS
            )
        }
    }
}