package com.example.logifitappp.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.logifitappp.R
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.layouts.CardLayout

@Composable
fun HomeLocationCard() {
    CardLayout(
        icon = Icons.Default.PinDrop,
        label = stringResource(id = R.string.my_location),

        suffixComponent = {
            Chip(
                label = stringResource(id = R.string.select).uppercase(),
                status = ChipStatusEnum.DANGER
            )
        }
    )
}