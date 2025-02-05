package com.example.logifitappp.ui.screens.steps_detail

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
import com.example.logifitappp.viewmodel.views.StepsDetailViewModel

@Composable
fun StepsDetailHeader(
    stepsDetailViewModel: StepsDetailViewModel
) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DateField(
            canGoToNextDay = stepsDetailViewModel.state.canGoToNextDay,
            date = stepsDetailViewModel.state.date.time,
            onDateSelected = { stepsDetailViewModel.handleChangeDateInMillis(it) },
            onNextDay = { stepsDetailViewModel.nextDay() },
            onPrevDay = { stepsDetailViewModel.prevDay() }
        )

        Text(
            text = stringResource(R.string.steps_taken),
            textAlign = TextAlign.Center
        )

        Text(
            text = stepsDetailViewModel.state.subtitle,
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.displayMedium
        )
    }
}