package com.example.logifitappp.ui.screens.stress_detail

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
import com.example.logifitappp.viewmodel.views.StressDetailViewModel

@Composable
fun StressDetailHeader(
    stressDetailViewModel: StressDetailViewModel
) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DateField(
            canGoToNextDay = stressDetailViewModel.state.canGoToNextDay,
            date = stressDetailViewModel.state.date.time,
            onDateSelected = { stressDetailViewModel.handleChangeDateInMillis(it) },
            onNextDay = { stressDetailViewModel.nextDay() },
            onPrevDay = { stressDetailViewModel.prevDay() }
        )

        Text(
            text = stringResource(R.string.stress),
            textAlign = TextAlign.Center
        )

        Text(
            text = stressDetailViewModel.state.subtitle,
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.displayMedium
        )
    }
}