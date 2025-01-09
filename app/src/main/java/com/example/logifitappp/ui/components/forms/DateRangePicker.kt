package com.example.logifitappp.ui.components.forms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.ui.components.Link
import com.example.logifitappp.ui.components.Text
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePicker(
    error: String? = null,
    onDateRangeSelected: (Long?, Long?) -> Unit,
    value: Pair<Long?, Long?>? = null
) {
    val state = rememberDateRangePickerState(initialSelectedStartDateMillis = value?.first, initialSelectedEndDateMillis = value?.second)
    var visible by remember { mutableStateOf(false) }

    if (visible) {
        DatePickerDialog(
            confirmButton = {
                Link(text = stringResource(id = R.string.button_ok)) {
                    onDateRangeSelected(
                        DateTimeUtils.transformUtcTimestampToSystemTimestamp(state.selectedStartDateMillis),
                        DateTimeUtils.transformUtcTimestampToSystemTimestamp(state.selectedEndDateMillis)
                    )

                    visible = false
                }
            },
            dismissButton = {
                Link(
                    text = stringResource(id = R.string.button_cancel),
                    textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ) {
                    visible = false
                }
            },
            onDismissRequest = { visible = false },
        ) {
            DateRangePicker(
                colors = DatePickerDefaults.colors(
                    dayContentColor = MaterialTheme.colorScheme.onSurface,
                    dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    dayInSelectionRangeContentColor = MaterialTheme.colorScheme.onPrimary,
                    dividerColor = Color.Transparent,
                    todayDateBorderColor = MaterialTheme.colorScheme.primary,
                    selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                    weekdayContentColor = MaterialTheme.colorScheme.surfaceTint
                ),
                headline = {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                        Box(Modifier.weight(1f)) {
                            (if (state.selectedStartDateMillis != null) state.selectedStartDateMillis?.let { DateTimeUtils.parse(it, "dd/MM/yyyy", TimeZone.getTimeZone("UTC")) } else stringResource(R.string.placeholder_start_date))?.let {
                                Text(
                                    text = it,
                                    typography = MaterialTheme.typography.headlineLarge
                                )
                            }
                        }

                        Box(Modifier.weight(1f)) {
                            (if (state.selectedEndDateMillis != null) state.selectedEndDateMillis?.let { DateTimeUtils.parse(it, "dd/MM/yyyy", TimeZone.getTimeZone("UTC")) } else stringResource(R.string.placeholder_end_date))?.let {
                                Text(
                                    text = it,
                                    typography = MaterialTheme.typography.headlineLarge
                                )
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(470.dp),
                state = state,
                showModeToggle = false,
                title = {
                    Text(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 24.dp).padding(top = 16.dp),
                        text = stringResource(R.string.select_date_range_title),
                        typography = MaterialTheme.typography.displayMedium
                    )
                }
            )
        }
    }

    Row(
        Modifier.clickable { visible = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            enabled = false,
            error = if (error.isNullOrEmpty()) null else " ",
            modifier = Modifier.weight(1f),
            onValueChange = {},
            placeholder = stringResource(R.string.placeholder_start_date),
            readOnly = true,
            trailingIcon = {
                Icon(
                    contentDescription = null,
                    imageVector = Icons.Filled.CalendarMonth,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
            },
            value = TextFieldValue(value?.first?.let { DateTimeUtils.parse(it, "dd/MM/yyyy", TimeZone.getTimeZone("UTC")) } ?: "")
        )

        Spacer(Modifier.width(1.dp))

        Text(
            text = "/",
            typography = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.width(1.dp))

        OutlinedTextField(
            enabled = false,
            error = error,
            modifier = Modifier.weight(1f),
            onValueChange = {},
            placeholder = stringResource(R.string.placeholder_end_date),
            readOnly = true,
            trailingIcon = {
                Icon(
                    contentDescription = null,
                    imageVector = Icons.Filled.CalendarMonth,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
            },
            value = TextFieldValue(value?.second?.let { DateTimeUtils.parse(it, "dd/MM/yyyy", TimeZone.getTimeZone("UTC")) } ?: "")
        )
    }
}