package com.example.logifitappp.ui.components.forms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.ui.components.Link
import com.example.logifitappp.ui.components.Text
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    onDateSelected: (Long?) -> Unit,
    trigger: @Composable () -> Unit
) {
    val state = rememberDatePickerState()
    var visible by remember { mutableStateOf(false) }

    if (visible) {
        DatePickerDialog(
            confirmButton = {
                Link(text = stringResource(id = R.string.button_ok)) {
                    onDateSelected(DateTimeUtils.transformUtcTimestampToSystemTimestamp(state.selectedDateMillis))
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
            androidx.compose.material3.DatePicker(
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
                    (if (state.selectedDateMillis != null) state.selectedDateMillis?.let { DateTimeUtils.parse(it, "dd/MM/yyyy", TimeZone.getTimeZone("UTC")) } else stringResource(R.string.placeholder_date))?.let {
                        Text(
                            text = it,
                            modifier = Modifier.padding(horizontal = 24.dp),
                            typography = MaterialTheme.typography.headlineLarge
                        )
                    }
                },
                showModeToggle = false,
                state = state,
                title = {
                    Text(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 24.dp).padding(top = 16.dp),
                        text = stringResource(R.string.select_date_title),
                        typography = MaterialTheme.typography.displayMedium
                    )
                }
            )
        }
    }

    Box(
        Modifier
            .wrapContentSize()
            .clickable { visible = true }
    ) {
        trigger()
    }
}