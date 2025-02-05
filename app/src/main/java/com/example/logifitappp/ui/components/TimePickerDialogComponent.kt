package com.example.logifitappp.ui.components

import android.widget.NumberPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog

@Composable
fun TimePickerDialogComponent(
    initialHours: Int = 0,
    initialMinutes: Int = 0,
    onDismiss: () -> Unit,
    onConfirm: (formattedTime: String) -> Unit
) {
    var selectedHours by remember { mutableIntStateOf(initialHours) }
    var selectedMinutes by remember { mutableIntStateOf(initialMinutes) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Select Time", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Hours Picker
                    AndroidView(
                        modifier = Modifier.weight(1f),
                        factory = { context ->
                            NumberPicker(context).apply {
                                minValue = 0
                                maxValue = 23
                                value = selectedHours
                                setOnValueChangedListener { _, _, newVal ->
                                    selectedHours = newVal
                                }
                            }
                        }
                    )

                    Text(text = "h", modifier = Modifier.padding(horizontal = 8.dp))

                    // Minutes Picker
                    AndroidView(
                        modifier = Modifier.weight(1f),
                        factory = { context ->
                            NumberPicker(context).apply {
                                minValue = 0
                                maxValue = 59
                                value = selectedMinutes
                                setOnValueChangedListener { _, _, newVal ->
                                    selectedMinutes = newVal
                                }
                            }
                        }
                    )

                    Text(text = "m", modifier = Modifier.padding(horizontal = 8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    TextButton(onClick = {
                        val formattedTime = formatTime(selectedHours, selectedMinutes)
                        onConfirm(formattedTime)
                    }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

// Function to format time into a human-readable string
fun formatTime(hours: Int, minutes: Int): String {
    return buildString {
        if (hours > 0) {
            append("$hours hours")
        }
        if (minutes > 0) {
            if (isNotEmpty()) append(" ") // Add space between hours and minutes
            append("$minutes minutes")
        }
        if (isEmpty()) append("0 minutes") // Default if both are 0
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomTimePickerDialog() {
    TimePickerDialogComponent (
        initialHours = 2,
        initialMinutes = 30,
        onDismiss = {},
        onConfirm = { formattedTime ->
            println("Selected Time: $formattedTime")
        }
    )
}