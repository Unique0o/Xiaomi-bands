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
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.components.forms.RadioButtonGroup
import com.example.logifitappp.ui.components.layouts.ModalLayout
import com.example.logifitappp.viewmodel.components.ChangeShiftModalViewModel

@Composable
fun ChangeShiftModal(
    onClose: () -> Unit,
    onDismissRequest: () -> Unit,
    onSelectShift: (shift: ShiftModel) -> Unit,
    shiftId: Int?,
    visible: Boolean
) {
    val changeShiftModalViewModel = hiltViewModel<ChangeShiftModalViewModel, ChangeShiftModalViewModel.ChangeShiftModalViewModelFactory>{
        it.create(shiftId)
    }

    ModalLayout(
        onClose = {
            onClose()
            changeShiftModalViewModel.fetchShift()
        },
        onDismissRequest = onDismissRequest,
        visible = visible
    ) {
        Text(
            color = MaterialTheme.colorScheme.primary,
            text = stringResource(id = R.string.change_shift_modal_title),
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.displayMedium
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.change_shift_modal_subtitle),
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(16.dp))

        RadioButtonGroup(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onChangeValue = { changeShiftModalViewModel.shift = it },
            options = changeShiftModalViewModel.shifts,
            value = changeShiftModalViewModel.shift
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.change_shift_modal_message),
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(16.dp))

        Button(
            enabled = changeShiftModalViewModel.shift != null,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onSelectShift(changeShiftModalViewModel.shift!!)
                onClose()
            },
            text = stringResource(id = R.string.button_ok)
        )
    }
}