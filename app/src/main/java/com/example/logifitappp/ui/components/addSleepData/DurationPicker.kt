package com.example.logifitappp.ui.components.addSleepData

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Text

@Composable
fun DurationPicker(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onDurationSelected: (String) -> Unit
) {
    if (showDialog) {
        var hours by remember { mutableStateOf("0") }
        var minutes by remember { mutableStateOf("00") }

        Dialog(onDismissRequest = onDismiss) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.outline
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.select_duration),
                        typography = MaterialTheme.typography.titleLarge
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = hours,
                            onValueChange = { value ->
                                if (value.isEmpty() || value.toIntOrNull() != null) {
                                    hours = value.take(2)
                                }
                            },
                            modifier = Modifier.width(70.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            label = { Text(text = "HH") },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        Text(
                            text = ":",
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        OutlinedTextField(
                            value = minutes,
                            onValueChange = { value ->
                                if (value.isEmpty() || (value.toIntOrNull() in 0..59)) {
                                    minutes = value.take(2)
                                }
                            },
                            modifier = Modifier.width(70.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            label = { Text(text = "MM") },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(text = stringResource(id = R.string.button_cancel))
                        }

                        Button(
                            onClick = {
                                val duration = "${hours}h ${minutes}m"
                                onDurationSelected(duration)
                                onDismiss()
                            },
                            enabled = hours.isNotEmpty() && minutes.isNotEmpty(),
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(text = stringResource(id = R.string.confirm))
                        }
                    }
                }
            }
        }
    }
}