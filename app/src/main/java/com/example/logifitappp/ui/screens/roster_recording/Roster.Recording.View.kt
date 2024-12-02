package com.example.logifitappp.ui.screens.roster_recording

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
import com.example.logifitappp.viewmodel.views.RosterRecordingViewModel

@Composable
fun RosterRecordingView(
    appViewModel: AppViewModel,
    navigation: NavHostController
) {
    val rosterRecordingViewModel = hiltViewModel<RosterRecordingViewModel, RosterRecordingViewModel.RosterRecordingViewModelFactory>{
        it.create(appViewModel.user!!)
    }

    MessageModal(
        onClose = {
            if (rosterRecordingViewModel.state.hasRosterInformationStorageBeenSuccessful) navigation.popBackStack()

            rosterRecordingViewModel.stopProcessing()
        },
        onDismissRequest = { rosterRecordingViewModel.stopProcessing() },
        status = rosterRecordingViewModel.state.status,
        visible = rosterRecordingViewModel.state.isLoading
    )

    SimplePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.roster_recording_title)
            )
        }
    ) {
        SelectableBottomSheet(
            elements = rosterRecordingViewModel.locations,
            error = rosterRecordingViewModel.state.locationError,
            onChange = { rosterRecordingViewModel.updateLocation(it) },
            placeholder = stringResource(R.string.placeholder_location),
            title = stringResource(R.string.location_title),
            value = rosterRecordingViewModel.state.location
        )

        Spacer(Modifier.height(6.dp))

        DateRangePickerModel(
            error = rosterRecordingViewModel.state.endDateError,
            onDateRangeSelected = { start, end -> rosterRecordingViewModel.updateRangeDate(start, end) },
            value = Pair(rosterRecordingViewModel.state.startDate?.timeInMillis, rosterRecordingViewModel.state.endDate?.timeInMillis)
        )

        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            onValueChange = { rosterRecordingViewModel.updateComment(it) },
            placeholder = stringResource(R.string.placeholder_comment),
            leadingIcon = Icons.Default.Edit,
            value = rosterRecordingViewModel.state.comment
        )

        Spacer(Modifier.weight(1f))

        IconButton(
            icon = Icons.Default.Save,
            onClick = { rosterRecordingViewModel.saveRoster() },
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.button_register)
        )
    }
}