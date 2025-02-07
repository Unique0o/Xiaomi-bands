package com.example.logifitappp.ui.components

import android.graphics.Typeface
import android.widget.NumberPicker
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog

@Composable
fun TimePickerDialogComponent(
    initialHours: Int = 0,
    initialMinutes: Int = 0,
    onDismiss: () -> Unit,
    onConfirm: (totalSeconds: Int) -> Unit
) {
    var selectedHours by remember { mutableIntStateOf(initialHours) }
    var selectedMinutes by remember { mutableIntStateOf(initialMinutes) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Time", typography = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Minutes Picker
                val fontColor = MaterialTheme.colorScheme.secondary
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Hours Picker
                    AndroidView(
                        modifier = Modifier.weight(1f),
                        factory = { context ->
                            val numberPicker = NumberPicker(context)
                            NumberPicker(context).apply {
                                minValue = 0
                                maxValue = 23
                                value = selectedHours
                                textColor = fontColor.toArgb()
                                setOnValueChangedListener { _, _, newVal ->
                                    selectedHours = newVal
                                }

                                fun setNumberPickerTextColor(picker: NumberPicker, color: Int) {
                                    for (i in 0 until picker.childCount) {
                                        val child = picker.getChildAt(i)
                                        if (child is TextView) {
                                            child.setTextColor(color)
                                            child.typeface = Typeface.DEFAULT_BOLD
                                            child.textSize = 20f
                                        }
                                    }
                                }

                                post { setNumberPickerTextColor(this, fontColor.toArgb()) }
                            }
                        }
                    )

                    Text(
                        text = "h", modifier = Modifier.padding(horizontal = 8.dp),
                        typography = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    AndroidView(
                        modifier = Modifier.weight(1f),
                        factory = { context ->
                            NumberPicker(context).apply {
                                minValue = 0
                                maxValue = 59
                                value = selectedMinutes
                                textColor = fontColor.toArgb()
                                setOnValueChangedListener { _, _, newVal ->
                                    selectedMinutes = newVal
                                }

                                fun setNumberPickerTextColor(picker: NumberPicker, color: Int) {
                                    for (i in 0 until picker.childCount) {
                                        val child = picker.getChildAt(i)
                                        if (child is TextView) {
                                            child.setTextColor(color)
                                            child.typeface = Typeface.DEFAULT_BOLD
                                            child.textSize = 20f
                                        }
                                    }
                                }

                                post { setNumberPickerTextColor(this, fontColor.toArgb()) }
                            }
                        }
                    )

                    Text(text = "m", modifier = Modifier.padding(horizontal = 8.dp),
                        typography = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(text = "Cancel",
                            typography = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary)
                    }
                    TextButton(onClick = {
                        val totalSeconds = convertHoursMinutesToSeconds(selectedHours, selectedMinutes)
                        onConfirm(totalSeconds)
                    }) {
                        Text(text = "OK",
                            typography = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary)
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

fun convertHoursMinutesToSeconds(hours: Int, minutes: Int): Int {
    return (hours * 3600) + (minutes * 60)
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomTimePickerDialog() {
    TimePickerDialogComponent(
        initialHours = 2,
        initialMinutes = 30,
        onDismiss = {},
        onConfirm = { formattedTime ->
            println("Selected Time: $formattedTime")
        }
    )
}