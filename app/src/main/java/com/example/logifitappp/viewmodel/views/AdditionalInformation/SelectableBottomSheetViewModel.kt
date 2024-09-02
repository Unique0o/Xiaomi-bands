package com.example.logifitappp.viewmodel.views.AdditionalInformation

import androidx.lifecycle.ViewModel
import com.example.logifitappp.ui.components.forms.SelectableItem
import com.example.logifitappp.ui.screens.additionalInformation.states.SelectableBottomSheetUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class SelectableBottomSheetViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SelectableBottomSheetUiState())
    val uiState: StateFlow<SelectableBottomSheetUiState> = _uiState.asStateFlow()

    fun setCountries(countries: List<SelectableItem>) {
        _uiState.update { currentState ->
            currentState.copy(countries = countries)
        }
    }

    fun onCountrySelected(country: SelectableItem) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCountry = country,
                countryError = null
            )
        }
    }

    fun setCountryError(error: String?) {
        _uiState.update { currentState ->
            currentState.copy(countryError = error)
        }
    }

    fun clearCountrySelection() {
        _uiState.update { currentState ->
            currentState.copy(selectedCountry = null)
        }
    }
}