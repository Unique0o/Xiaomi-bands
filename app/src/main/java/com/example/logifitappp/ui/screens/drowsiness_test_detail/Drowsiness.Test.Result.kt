package com.example.logifitappp.ui.screens.drowsiness_test_detail

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardBackspace
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.data.models.EvaluationResultModel
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.cards.SleepProcessingComponent
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.components.layouts.CardLayout
import com.example.logifitappp.ui.components.pages.SimplePage

@Composable
fun DrowsinessTestResult(
    navigation: NavHostController,
    result: EvaluationResultModel,
    shouldItBackUntilHome: Boolean
) {
    SimplePage {
        IconButton(
            backgroundColor = Color.Transparent,
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
            horizontalPadding = 0.dp,
            icon = Icons.AutoMirrored.Rounded.KeyboardBackspace,
            onClick = {
                if (shouldItBackUntilHome) navigation.navigate(MainRoutes.SplashScreen)
                else navigation.popBackStack()
            },
            text = stringResource(id = R.string.go_back),
            textColor = MaterialTheme.colorScheme.primary
        )

        CardLayout(Modifier.fillMaxWidth()) {
            SleepProcessingComponent(
                label = result.title,
                sleepProcessingStatusEnum = result.calculateStatus()
            )

            Text(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                text = result.result,
                textAlign = TextAlign.Center,
                typography = MaterialTheme.typography.labelMedium
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}