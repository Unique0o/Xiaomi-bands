package com.example.logifitappp.ui.components.time

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
import androidx.compose.ui.platform.LocalContext
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.ui.res.stringResource
import java.util.*
import com.example.logifitappp.R

@Composable
fun TimePicker(
    onDateTimeSelected: (LocalDateTime) -> Unit
) {
    val context = LocalContext.current
    var selectedDateTime by remember { mutableStateOf<LocalDateTime?>(null) }
    val calendar = remember { Calendar.getInstance() }

    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = {
                val currentDate = Calendar.getInstance()

                val minDate = Calendar.getInstance()
                minDate.add(Calendar.DAY_OF_MONTH, -2)
                minDate.set(Calendar.HOUR_OF_DAY, 0)
                minDate.set(Calendar.MINUTE, 0)
                minDate.set(Calendar.SECOND, 0)
                minDate.set(Calendar.MILLISECOND, 0)

                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        calendar.set(year, month, dayOfMonth)

                        TimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                val selectedCalendar = Calendar.getInstance().apply {
                                    set(year, month, dayOfMonth, hourOfDay, minute, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }

                                val now = Calendar.getInstance()

                                if (isToday(selectedCalendar) && selectedCalendar.timeInMillis > now.timeInMillis) {
                                    selectedCalendar.timeInMillis = now.timeInMillis
                                }


                                val localDateTime = LocalDateTime.of(
                                    selectedCalendar.get(Calendar.YEAR),
                                    selectedCalendar.get(Calendar.MONTH) + 1,
                                    selectedCalendar.get(Calendar.DAY_OF_MONTH),
                                    selectedCalendar.get(Calendar.HOUR_OF_DAY),
                                    selectedCalendar.get(Calendar.MINUTE)
                                )

                                selectedDateTime = localDateTime
                                onDateTimeSelected(localDateTime)
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    currentDate.get(Calendar.YEAR),
                    currentDate.get(Calendar.MONTH),
                    currentDate.get(Calendar.DAY_OF_MONTH)
                ).apply {

                    datePicker.maxDate = currentDate.timeInMillis
                    datePicker.minDate = minDate.timeInMillis
                }.show()
            }
        ) {
            Text(stringResource(id = R.string.select))
        }
    }
}

private fun isToday(calendar: Calendar): Boolean {
    val today = Calendar.getInstance()
    return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
}
