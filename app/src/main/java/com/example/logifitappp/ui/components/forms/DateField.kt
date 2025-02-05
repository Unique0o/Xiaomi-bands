package com.example.logifitappp.ui.components.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.theme.Zinc680
import java.util.Date

@Composable
fun DateField(
    canGoToNextDay: Boolean,
    date: Date,
    onDateSelected: (Long?) -> Unit,
    onNextDay: () -> Unit,
    onPrevDay: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material3.IconButton(
            modifier = Modifier.padding(8.dp),
            onClick = onPrevDay
        ) {
            Icon(
                contentDescription = null,
                imageVector = Icons.Default.ChevronLeft,
                modifier = Modifier.size(12.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(Modifier.width(16.dp))

        DatePicker(onDateSelected = onDateSelected) {
            Text(
                color = MaterialTheme.colorScheme.outlineVariant,
                text = DateTimeUtils.format(date, "MMMM dd, yyyy"),
                typography = MaterialTheme.typography.labelMedium
            )
        }

        Spacer(Modifier.width(16.dp))

        androidx.compose.material3.IconButton(
            enabled = canGoToNextDay,
            modifier = Modifier.padding(8.dp),
            onClick = onNextDay
        ) {
            Icon(
                contentDescription = null,
                imageVector = Icons.Default.ChevronRight,
                modifier = Modifier.size(12.dp),
                tint = if (canGoToNextDay) MaterialTheme.colorScheme.onSurface else Zinc680.copy(alpha = 0.5f)
            )
        }
    }
}