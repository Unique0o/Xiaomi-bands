package com.example.logifitappp.ui.screens.additionalInformation.states

import com.example.logifitappp.ui.components.forms.SelectableItem

data class SelectableBottomSheetUiState(
    val countries: List<SelectableItem> = emptyList(),
    val selectedCountry: SelectableItem? = null,
    val countryError: String? = null
)
