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
import com.example.logifitappp.core.utils.DurationUtils
import com.example.logifitappp.data.models.DrowsinessModel
import com.example.logifitappp.data.models.SleepConditionModel
import com.example.logifitappp.data.models.TenantModel
import com.example.logifitappp.enums.SleepProcessingStatusEnum
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.MarkdownText
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.cards.SleepParameterCard
import com.example.logifitappp.ui.components.layouts.ModalLayout

@Composable
fun DrowsinessDetailModal(
    onClose: () -> Unit,
    onDismissRequest: () -> Unit,
    drowsiness: DrowsinessModel?,
    drowsinessCondition: SleepConditionModel?,
    tenant: TenantModel?,
    visible: Boolean
) {
    ModalLayout(
        onClose = onClose,
        onDismissRequest = onDismissRequest,
        visible = visible
    ) {
        val condition = drowsinessCondition?.calculateStatus() ?: SleepProcessingStatusEnum.PENDING

        Text(
            color = MaterialTheme.colorScheme.primary,
            text = stringResource(id = R.string.drowsiness_detail_modal_title),
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
                text = stringResource(R.string.drowsiness_detail_modal_subtitle),
                typography = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.width(8.dp))

            Chip(
                label = drowsinessCondition?.name?.uppercase() ?: stringResource(id = condition.label).uppercase(),
                labelTypography = MaterialTheme.typography.titleSmall,
                status = condition.chipStatus
            )
        }

        Spacer(Modifier.height(12.dp))

        MarkdownText(
            text = stringResource(R.string.drowsiness_detail_modal_message, (tenant?.sleepAnalysisHours ?: (6 * 3600)) / 3600),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        SleepParameterCard(
            image = R.drawable.ic_sleep_information,
            label = stringResource(R.string.total_sleep_time)
        ) {
            Chip(
                label = DurationUtils.format(drowsiness?.totalSleepSeconds ?: 0),
                labelTypography = MaterialTheme.typography.titleSmall,
                status = condition.chipStatus
            )
        }
    }
}