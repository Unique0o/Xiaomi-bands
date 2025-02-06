package com.example.logifitappp.ui.screens.wearable_key_update

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.avoidBottom
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.viewmodel.views.WearableKeyUpdateViewModel

@Composable
fun WearableKeyUpdateMacList(
    wearableKeyUpdateViewModel: WearableKeyUpdateViewModel
) {
    Row(
        Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary).padding(start = 14.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { wearableKeyUpdateViewModel.clearOrAddAllMacs() }) {
            Icon(
                contentDescription = null,
                imageVector = if (wearableKeyUpdateViewModel.selectedMacs.isEmpty()) Icons.Default.CheckBoxOutlineBlank else Icons.Default.Close,
                modifier = Modifier.size(25.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        if (wearableKeyUpdateViewModel.selectedMacs.isNotEmpty()) {
            Button(
                backgroundColor = MaterialTheme.colorScheme.onPrimary,
                colorText = MaterialTheme.colorScheme.primary,
                onClick = { wearableKeyUpdateViewModel.storeSelectedMacs() },
                text = stringResource(R.string.button_register)
            )
        }
    }
    
    Column(Modifier.padding(PaddingValues(16.dp).avoidBottom())) {
        wearableKeyUpdateViewModel.macs.forEach {
            InformationCard(
                icon = if (wearableKeyUpdateViewModel.selectedMacs.contains(it)) Icons.Default.Check else Icons.Default.CheckBoxOutlineBlank,
                modifier = Modifier.clickable { wearableKeyUpdateViewModel.addOrRemoveMac(it) },
                label = it.mac,
                suffixComponent = {
                    it.type?.let { type ->
                        Chip(
                            label = type,
                            status = ChipStatusEnum.INFO
                        )
                    }
                }
            ) {
                Box(Modifier.offset(y = (-12).dp)) {
                    IconText(
                        icon = Icons.Default.Key,
                        iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        iconSize = 20.dp,
                        label = it.token,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        labelTypography = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}