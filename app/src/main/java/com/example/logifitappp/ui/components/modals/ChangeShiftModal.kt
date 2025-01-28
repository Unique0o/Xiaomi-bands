package com.example.logifitappp.ui.components.modals

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.components.forms.RadioButtonGroup
import com.example.logifitappp.ui.components.layouts.ModalLayout

@Composable
fun ChangeShiftModal(
    onClose: () -> Unit,
    onDismissRequest: () -> Unit,
    onSelectShift: (shift: ShiftModel) -> Unit,
    shiftId: Int?,
    visible: Boolean
) {
    val shifts by remember { mutableStateOf(App.database.userDao().getLoggedIn()?.let { user -> App.database.shiftDao().all(user.tenantId) } ?: listOf()) }
    var shift by remember(shiftId) { mutableStateOf(shifts.find { shift -> shift.id == shiftId }) }

    ModalLayout(
        onClose = onClose,
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
            onChangeValue = { shift = it },
            options = shifts,
            value = shift
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.change_shift_modal_message),
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(16.dp))

        Button(
            enabled = shift != null,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                shift?.let { onSelectShift(it) }
                onClose()
            },
            text = stringResource(id = R.string.button_ok)
        )
    }
}