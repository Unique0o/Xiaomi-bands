package com.example.logifitappp.ui.screens.spo2_detail

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
import com.example.logifitappp.viewmodel.views.Spo2DetailViewModel

@Composable
fun Spo2DetailHeader(
    spo2DetailViewModel: Spo2DetailViewModel
) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DateField(
            canGoToNextDay = spo2DetailViewModel.state.canGoToNextDay,
            date = spo2DetailViewModel.state.date.time,
            onDateSelected = { spo2DetailViewModel.handleChangeDateInMillis(it) },
            onNextDay = { spo2DetailViewModel.nextDay() },
            onPrevDay = { spo2DetailViewModel.prevDay() }
        )

        Text(
            text = stringResource(R.string.spo2),
            textAlign = TextAlign.Center
        )

        Text(
            text = spo2DetailViewModel.state.subtitle,
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.displayMedium
        )
    }
}