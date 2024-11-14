package com.example.logifitappp.ui.components.modals

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.logifitappp.R
import com.example.logifitappp.data.models.LocationModel
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.components.forms.RadioButtonGroup
import com.example.logifitappp.ui.components.layouts.ModalLayout
import com.example.logifitappp.viewmodel.components.ChangeLocationModalViewModel

@Composable
fun ChangeLocationModal(
    onClose: () -> Unit,
    onDismissRequest: () -> Unit,
    onSelectLocation: (location: LocationModel) -> Unit,
    locationId: Int?,
    visible: Boolean
) {
    val changeLocationModalViewModel = hiltViewModel <ChangeLocationModalViewModel, ChangeLocationModalViewModel.ChangeLocationModalViewModelFactory> {
        it.create(locationId)
    }

    ModalLayout(
        onClose = {
            onClose()
            changeLocationModalViewModel.fetchLocation()
        },
        onDismissRequest = onDismissRequest,
        visible = visible
    ) {
        Text(
            color = MaterialTheme.colorScheme.primary,
            text = stringResource(id = R.string.change_location_modal_title),
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.displayMedium
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.change_location_modal_subtitle),
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(16.dp))

        RadioButtonGroup(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onChangeValue = { changeLocationModalViewModel.location = it },
            options = changeLocationModalViewModel.locations,
            value = changeLocationModalViewModel.location
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.change_location_modal_message),
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(16.dp))

        Button(
            enabled = changeLocationModalViewModel.location != null,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onSelectLocation(changeLocationModalViewModel.location!!)
                onClose()
            },
            text = stringResource(id = R.string.button_ok)
        )
    }
}