package com.example.logifitappp.ui.screens.wearable_key_update

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.avoidTop
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.components.forms.SelectableBottomSheet
import com.example.logifitappp.viewmodel.views.WearableKeyUpdateViewModel

@Composable
fun WearableKeyUpdateFilters(
    wearableKeyUpdateViewModel: WearableKeyUpdateViewModel
) {
    Column(Modifier.fillMaxWidth().padding(PaddingValues(16.dp).avoidTop())) {
        SelectableBottomSheet(
            elements = wearableKeyUpdateViewModel.types,
            onChange = { wearableKeyUpdateViewModel.updateType(it) },
            placeholder = stringResource(R.string.placeholder_xiaomi_credential_type),
            title = stringResource(R.string.xiaomi_credential_type_title),
            value = wearableKeyUpdateViewModel.state.type
        )

        SelectableBottomSheet(
            elements = wearableKeyUpdateViewModel.credentials,
            onChange = { wearableKeyUpdateViewModel.updateCredential(it) },
            placeholder = stringResource(R.string.placeholder_xiaomi_credential),
            title = stringResource(R.string.xiaomi_credential_title),
            value = wearableKeyUpdateViewModel.state.credential
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { wearableKeyUpdateViewModel.fetchMacs() },
            text = stringResource(R.string.button_search)
        )
    }
}