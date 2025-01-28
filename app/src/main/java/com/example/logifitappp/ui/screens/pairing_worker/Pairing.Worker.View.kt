package com.example.logifitappp.ui.screens.pairing_worker

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.data.remote.dto.response.WorkerItemResponse
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.AutocompleteDropdown
import com.example.logifitappp.ui.components.forms.AutocompleteDropdownItem
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.modals.MessageModal
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.PairingWorkerViewModel

@Composable
fun PairingWorkerView(
    appViewModel: AppViewModel,
    navigation: NavHostController,
    mac: String,
    workers: Array<WorkerItemResponse>
) {
    val pairingWorkerViewModel = hiltViewModel<PairingWorkerViewModel, PairingWorkerViewModel.PairingWorkerViewModelFactory> {
        it.create(navigation, mac, appViewModel.user)
    }

    MessageModal(
        onClose = { pairingWorkerViewModel.stopProcessing() },
        onDismissRequest = { pairingWorkerViewModel.stopProcessing() },
        status = pairingWorkerViewModel.state.pairingStatus,
        visible = pairingWorkerViewModel.state.isPairing
    )

    SimplePage(
        backgroundColor = MaterialTheme.colorScheme.surface,
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.pairing_worker_title)
            )
        }
    ) {
        Text(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            text = stringResource(id = R.string.pairing_worker_message, pairingWorkerViewModel.state.wearable?.getName() ?: "N/A", pairingWorkerViewModel.state.wearable?.getAddress() ?: "N/A"),
            typography = MaterialTheme.typography.labelMedium
        )

        AutocompleteDropdown(
            placeholder = stringResource(R.string.placeholder_search),
            onSuggestionSelected = { item -> pairingWorkerViewModel.handleSelectedWorker(workers.find { it.id == item.id }) },
            suggestions = workers.map { AutocompleteDropdownItem(it.id, "${it.document ?: "N/A"} - ${it.firstName} ${it.lastName}") }
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            enabled = pairingWorkerViewModel.state.selectedWorker != null,
            icon = Icons.Default.Link,
            onClick = { pairingWorkerViewModel.pair() },
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.button_pair_operator)
        )
    }
}