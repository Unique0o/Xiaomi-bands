package com.example.logifitappp.ui.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.ui.components.Text

@Composable
fun ModalLayout(
    onClose: () -> Unit,
    onDismissRequest: () -> Unit,
    status: AppStatusCodeEnum,
    visible: Boolean
) {
    if (!visible) return

    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.93f)
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .padding(7.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(12.dp))
                            .padding(24.dp)
                            .fillMaxWidth()
                    ) {
                        status.component?.let {
                            it()
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        Text(
                            text = stringResource(id = status.message),
                            typography = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                if (!status.keepOpen) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .background(MaterialTheme.colorScheme.onSurface, CircleShape)
                            .size(20.dp)
                    ) {
                        Icon(
                            contentDescription = null,
                            imageVector = Icons.Filled.Close,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}