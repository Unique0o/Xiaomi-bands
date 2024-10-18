package com.example.logifitappp.viewmodel.views.AdditionalInformation

import androidx.compose.ui.text.input.TextFieldValue
import com.example.logifitappp.data.models.CountryModel
import com.example.logifitappp.data.models.DocumentTypeModel

data class AdditionalInformationState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedCountry: CountryModel? = null,
    val selectedDocumentType: DocumentTypeModel? = null,
    val name: TextFieldValue = TextFieldValue(""),
    val lastnames: TextFieldValue = TextFieldValue(""),
    val documentIdentity: TextFieldValue = TextFieldValue(""),
    val mobile: TextFieldValue = TextFieldValue(""),
    val countries: List<CountryModel> = emptyList(),
    val formErrors: Map<String, String> = emptyMap()
)