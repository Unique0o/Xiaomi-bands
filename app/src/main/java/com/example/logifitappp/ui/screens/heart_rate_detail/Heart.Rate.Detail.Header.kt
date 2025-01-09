package com.example.logifitappp.ui.screens.heart_rate_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.DateField
import com.example.logifitappp.viewmodel.views.HeartRateDetailViewModel

@Composable
fun HeartRateDetailHeader(
    heartRateDetailViewModel: HeartRateDetailViewModel
) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DateField(
            canGoToNextDay = heartRateDetailViewModel.state.canGoToNextDay,
            date = heartRateDetailViewModel.state.date.time,
            onDateSelected = { heartRateDetailViewModel.handleChangeDateInMillis(it) },
            onNextDay = { heartRateDetailViewModel.nextDay() },
            onPrevDay = { heartRateDetailViewModel.prevDay() }
        )

        Text(
            text = stringResource(R.string.heart_rate),
            textAlign = TextAlign.Center
        )

        Text(
            text = heartRateDetailViewModel.state.subtitle,
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.displayMedium
        )
    }
}