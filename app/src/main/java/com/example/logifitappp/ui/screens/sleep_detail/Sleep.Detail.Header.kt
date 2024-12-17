package com.example.logifitappp.ui.screens.sleep_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.DatePicker
import com.example.logifitappp.ui.theme.Zinc680
import com.example.logifitappp.viewmodel.views.SleepDetailViewModel

@Composable
fun SleepDetailHeader(
    sleepDetailViewModel:  SleepDetailViewModel
) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.padding(8.dp),
                onClick = { sleepDetailViewModel.prevDay() }
            ) {
                Icon(
                    contentDescription = "prev lesson",
                    imageVector = Icons.Default.ChevronLeft,
                    modifier = Modifier.size(12.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(Modifier.width(16.dp))

            DatePicker(
                onDateSelected = { sleepDetailViewModel.handleChangeDateInMillis(it) }
            ) {
                Text(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    text = DateTimeUtils.format(sleepDetailViewModel.state.date.time, "MMMM dd, yyyy"),
                    typography = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(Modifier.width(16.dp))

            IconButton(
                enabled = sleepDetailViewModel.state.canGoToNextDay,
                modifier = Modifier.padding(8.dp),
                onClick = { sleepDetailViewModel.nextDay() }
            ) {
                Icon(
                    contentDescription = "next lesson",
                    imageVector = Icons.Default.ChevronRight,
                    modifier = Modifier.size(12.dp),
                    tint = if (sleepDetailViewModel.state.canGoToNextDay) MaterialTheme.colorScheme.onSurface else Zinc680.copy(alpha = 0.5f)
                )
            }
        }

        Text(
            text = sleepDetailViewModel.state.title,
            textAlign = TextAlign.Center
        )

        Text(
            text = sleepDetailViewModel.state.subtitle,
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.displayMedium
        )

        Text(
            color = MaterialTheme.colorScheme.primary,
            text = sleepDetailViewModel.state.summary,
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.bodyMedium
        )
    }
}