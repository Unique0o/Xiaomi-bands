package com.example.logifitappp.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.logifitappp.R
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.modals.ChangeShiftModal

@Composable
fun HomeShiftCard(
    onSelectShift: (shift: ShiftModel) -> Unit,
    shift: ShiftModel?
) {
    var isChangeShiftModalVisible by remember { mutableStateOf(false) }

    ChangeShiftModal(
        onClose = { isChangeShiftModalVisible = false },
        onDismissRequest = { isChangeShiftModalVisible = false },
        onSelectShift = onSelectShift,
        shiftId = shift?.id,
        visible = isChangeShiftModalVisible
    )

    InformationCard(
        icon = Icons.Default.Schedule,
        label = stringResource(id = R.string.schedule),
        modifier = Modifier.clickable {
            isChangeShiftModalVisible = true
        },

        suffixComponent = {
            Chip(
                label = shift?.name?.uppercase() ?: stringResource(id = R.string.select).uppercase(),
                status = if (shift == null) ChipStatusEnum.DANGER else ChipStatusEnum.SUCCESS
            )
        }
    )
}