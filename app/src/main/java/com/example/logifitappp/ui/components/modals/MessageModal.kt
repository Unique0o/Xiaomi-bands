package com.example.logifitappp.ui.components.modals

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.layouts.ModalLayout

@Composable
fun MessageModal(
    onClose: () -> Unit,
    onDismissRequest: () -> Unit,
    status: AppStatusCodeEnum,
    visible: Boolean
) {
    ModalLayout(
        onClose = onClose,
        onDismissRequest = onDismissRequest,
        keepOpen = status.keepOpen,
        visible = visible
    ) {
        status.component?.let {
            it()
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = stringResource(id = status.message),
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.bodyMedium
        )
    }
}