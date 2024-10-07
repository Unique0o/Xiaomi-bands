package com.example.logifitappp.ui.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.logifitappp.R
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.Button

@Composable
fun ModalLayout(
    onClose: () -> Unit,
    onDismissRequest: () -> Unit,
    status: AppStatusCodeEnum
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                Button(
                    onClick = onClose,
                    text = stringResource(id = R.string.button_close)
                )

                Surface (
                    modifier = Modifier.wrapContentSize()
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    status.component?.let {
                        it()
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Text(
                        text = stringResource(id = status.message)
                    )
                }
            }
        }
    }
}