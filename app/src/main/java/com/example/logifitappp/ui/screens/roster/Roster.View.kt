package com.example.logifitappp.ui.screens.roster

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.components.forms.OutlinedTextField
import com.example.logifitappp.ui.components.forms.SelectableBottomSheet
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.modals.DateRangePickerModel
import com.example.logifitappp.ui.components.modals.MessageModal
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.RosterViewModel

@Composable
fun RosterView(
    appViewModel: AppViewModel,
    navigation: NavHostController
) {
    val rosterViewModel = hiltViewModel<RosterViewModel, RosterViewModel.RosterViewModelFactory>{
        it.create(appViewModel.user!!)
    }

    MessageModal(
        onClose = {
            if (rosterViewModel.state.hasRosterInformationStorageBeenSuccessful) navigation.popBackStack()

            rosterViewModel.stopProcessing()
        },
        onDismissRequest = { rosterViewModel.stopProcessing() },
        status = rosterViewModel.state.status,
        visible = rosterViewModel.state.isLoading
    )

    SimplePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.roster_title)
            )
        }
    ) {
        SelectableBottomSheet(
            elements = rosterViewModel.locations,
            error = rosterViewModel.state.locationError,
            onChange = { rosterViewModel.updateLocation(it) },
            placeholder = stringResource(R.string.placeholder_location),
            title = stringResource(R.string.location_title),
            value = rosterViewModel.state.location
        )

        Spacer(Modifier.height(6.dp))

        DateRangePickerModel(
            error = rosterViewModel.state.endDateError,
            onDateRangeSelected = { start, end -> rosterViewModel.updateRangeDate(start, end) },
            value = Pair(rosterViewModel.state.startDate?.timeInMillis, rosterViewModel.state.endDate?.timeInMillis)
        )

        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            onValueChange = { rosterViewModel.updateComment(it) },
            placeholder = stringResource(R.string.placeholder_comment),
            leadingIcon = Icons.Default.Edit,
            value = rosterViewModel.state.comment
        )

        Spacer(Modifier.weight(1f))

        IconButton(
            icon = Icons.Default.Save,
            onClick = { rosterViewModel.saveRoster() },
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.button_register)
        )
    }
}