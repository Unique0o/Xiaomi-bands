package com.example.logifitappp.ui.screens.termsConditions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.viewmodel.views.termsConditions.TermsAndConditionsUiState
import com.example.logifitappp.viewmodel.views.termsConditions.TermsAndConditionsViewModel

@Composable
fun TermsAndConditionsScreen(navigation: NavHostController) {
    val viewModel: TermsAndConditionsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Términos y Condiciones",
        )
        Spacer(modifier = Modifier.height(16.dp))

        when (val state = uiState) {
            is TermsAndConditionsUiState.Loading -> {
                CircularProgressIndicator()
            }
            is TermsAndConditionsUiState.Success -> {
                Text(
                    text = state.message,
                    color = Color.Green
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = state.termsAndConditions.content,
                )
            }
            is TermsAndConditionsUiState.Error -> {
                Text(
                    text = state.message,
                    color = Color.Red
                )
            }
        }
    }
}

