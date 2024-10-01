package com.example.logifitappp.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.logifitappp.R
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.ui.components.layouts.CardLayout
import com.example.logifitappp.ui.components.Chip

@Composable
fun HomeShiftCard() {
    CardLayout(
        icon = Icons.Default.Schedule,
        label = stringResource(id = R.string.schedule),

        suffixComponent = {
            Chip(
                label = "DIURNO",
                status = ChipStatusEnum.SUCCESS
            )
        }
    )
}