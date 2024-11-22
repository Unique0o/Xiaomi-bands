package com.example.logifitappp.ui.components.time

import androidx.compose.foundation.BorderStroke
import com.example.logifitappp.ui.components.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import androidx.compose.material3.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.logifitappp.R

@Composable
fun TimePicker(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onDateTimeSelected: (LocalDateTime) -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
            var hours by remember { mutableStateOf("12") }
            var minutes by remember { mutableStateOf("00") }
            var isPM by remember { mutableStateOf(false) }
            var expanded by remember { mutableStateOf(false) }

            val dates = remember {
                (0..2).map { daysAgo ->
                    LocalDate.now().minusDays(daysAgo.toLong())
                }
            }

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
                        stringResource(id = R.string.select_date_and_time),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        dates.forEach { date ->
                            OutlinedButton(
                                onClick = { selectedDate = date },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (selectedDate == date) MaterialTheme.colorScheme.primaryContainer
                                    else
                                        MaterialTheme.colorScheme.outline,
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp)
                            ) {
                                Text(
                                    text = date.format(DateTimeFormatter.ofPattern("MMM dd")),
                                    color = if (selectedDate == date)
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = hours,
                            onValueChange = { value ->
                                if (value.isEmpty() || (value.toIntOrNull() in 1..12)) {
                                    hours = value.take(2)
                                }
                            },
                            modifier = Modifier.width(70.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            label = { Text("HH") },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        Text(":", modifier = Modifier.padding(horizontal = 8.dp))

                        OutlinedTextField(
                            value = minutes,
                            onValueChange = { value ->
                                if (value.isEmpty() || (value.toIntOrNull() in 0..59)) {
                                    minutes = value.take(2).padStart(2, '0')
                                }
                            },
                            modifier = Modifier.width(70.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            label = { Text("MM") },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        Box(modifier = Modifier.padding(start = 8.dp)) {
                            OutlinedButton(
                                onClick = { expanded = true }
                            ) {
                                Text(if (isPM) "PM" else "AM")
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("AM") },
                                    onClick = {
                                        isPM = false
                                        expanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("PM") },
                                    onClick = {
                                        isPM = true
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(stringResource(id = R.string.button_cancel))
                        }

                        Button(
                            onClick = {
                                selectedDate?.let { date ->
                                    val hour24 = when {
                                        isPM && hours != "12" -> hours.toInt() + 12
                                        !isPM && hours == "12" -> 0
                                        else -> hours.toInt()
                                    }
                                    val dateTime = LocalDateTime.of(
                                        date.year,
                                        date.month,
                                        date.dayOfMonth,
                                        hour24,
                                        minutes.toInt()
                                    )
                                    onDateTimeSelected(dateTime)
                                    onDismiss()
                                }
                            },
                            enabled = selectedDate != null && hours.isNotEmpty() && minutes.isNotEmpty(),
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(stringResource(id = R.string.select))
                        }
                    }
                }
            }
        }
    }
}