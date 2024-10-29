package com.example.logifitappp.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.logifitappp.R
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.ui.components.Chip

@Composable
fun HomeShiftCard(
    shift: ShiftModel?
) {
    InformationCard(
        icon = Icons.Default.Schedule,
        label = stringResource(id = R.string.schedule),

        suffixComponent = {
            Chip(
                label = shift?.name?.uppercase() ?: stringResource(id = R.string.select).uppercase(),
                status = if (shift == null) ChipStatusEnum.DANGER else ChipStatusEnum.SUCCESS
            )
        }
    )
}