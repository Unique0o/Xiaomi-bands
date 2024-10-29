package com.example.logifitappp.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.logifitappp.R
import com.example.logifitappp.data.models.LocationModel
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.cards.InformationCard

@Composable
fun HomeLocationCard(
    location: LocationModel?
) {
    InformationCard(
        icon = Icons.Default.PinDrop,
        label = stringResource(id = R.string.my_location),

        suffixComponent = {
            Chip(
                label = location?.name?.uppercase() ?: stringResource(id = R.string.select).uppercase(),
                status = if (location == null) ChipStatusEnum.DANGER else ChipStatusEnum.SUCCESS
            )
        }
    )
}