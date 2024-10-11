package com.example.logifitappp.viewmodel.views.termsConditions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.domain.usecase.GetTermsAndConditionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsAndConditionsViewModel @Inject constructor (
    private val getTermsAndConditionsUseCase: GetTermsAndConditionsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<TermsAndConditionsUiState>(TermsAndConditionsUiState.Loading)
    val uiState: StateFlow<TermsAndConditionsUiState> = _uiState

    init {
        loadTermsAndConditions()
    }

    private fun loadTermsAndConditions() {
        viewModelScope.launch {
            _uiState.value = TermsAndConditionsUiState.Loading
            val result = getTermsAndConditionsUseCase()

            _uiState.value = result.fold(
                onSuccess = { response ->
                    if (response.generalResponse.success) {
                        TermsAndConditionsUiState.Success(response.termsAndConditions, response.generalResponse.message)
                    } else {
                        TermsAndConditionsUiState.Error(response.generalResponse.message)
                    }
                },
                onFailure = { TermsAndConditionsUiState.Error(it.message ?: "Unknown error occurred") }
            )
        }
    }
}