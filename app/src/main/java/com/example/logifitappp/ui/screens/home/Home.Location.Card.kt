package com.example.logifitappp.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.logifitappp.R
import com.example.logifitappp.data.models.LocationModel
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.ui.components.modals.ChangeLocationModal

@Composable
fun HomeLocationCard(
    onSelectLocation: (shift: LocationModel) -> Unit,
    location: LocationModel?
) {
    var isChangeLocationModalVisible by remember { mutableStateOf(false) }

    ChangeLocationModal(
        onClose = { isChangeLocationModalVisible = false },
        onDismissRequest = { isChangeLocationModalVisible = false },
        onSelectLocation = onSelectLocation,
        locationId = location?.id,
        visible = isChangeLocationModalVisible
    )

    InformationCard(
        icon = Icons.Default.PinDrop,
        label = stringResource(id = R.string.my_location),
        modifier = Modifier.clickable {
            isChangeLocationModalVisible = true
        },

        suffixComponent = {
            Chip(
                label = location?.name?.uppercase() ?: stringResource(id = R.string.select).uppercase(),
                status = if (location == null) ChipStatusEnum.DANGER else ChipStatusEnum.SUCCESS
            )
        }
    )
}