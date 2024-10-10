package com.example.logifitappp.viewmodel.views.termsConditions

import com.example.logifitappp.data.models.TermsAndConditions

sealed class TermsAndConditionsUiState {
    object Loading : TermsAndConditionsUiState()
    data class Success(val termsAndConditions: TermsAndConditions, val message: String) : TermsAndConditionsUiState()
    data class Error(val message: String) : TermsAndConditionsUiState()
}