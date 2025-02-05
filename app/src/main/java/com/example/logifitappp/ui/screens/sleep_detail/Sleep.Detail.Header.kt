package com.example.logifitappp.ui.screens.sleep_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.DateField
import com.example.logifitappp.viewmodel.views.SleepDetailViewModel

@Composable
fun SleepDetailHeader(
    sleepDetailViewModel:  SleepDetailViewModel
) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DateField(
            canGoToNextDay = sleepDetailViewModel.state.canGoToNextDay,
            date = sleepDetailViewModel.state.date.time,
            onDateSelected = { sleepDetailViewModel.handleChangeDateInMillis(it) },
            onNextDay = { sleepDetailViewModel.nextDay() },
            onPrevDay = { sleepDetailViewModel.prevDay() }
        )

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