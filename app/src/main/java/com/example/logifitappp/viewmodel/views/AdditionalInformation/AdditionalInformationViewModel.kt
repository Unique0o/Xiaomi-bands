package com.example.logifitappp.viewmodel.views.AdditionalInformation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.R
import com.example.logifitappp.data.models.CountryPhoneCode
import com.example.logifitappp.ui.components.forms.SelectableItem

class AdditionalInformationViewModel : ViewModel() {


    var name by mutableStateOf(TextFieldValue(""))
        private set

    var lastnames by mutableStateOf(TextFieldValue(""))
        private set
    var type_document by mutableStateOf(TextFieldValue(""))
        private set
    var document_identity by mutableStateOf(TextFieldValue(""))
        private set

    var country by mutableStateOf(TextFieldValue(""))
        private set

    var mobile by mutableStateOf(TextFieldValue(""))
        private set

    fun updateName(name: TextFieldValue) {
        this.name = name
    }

    fun updateLastnames(lastnames: TextFieldValue) {
        this.lastnames = lastnames
    }

    fun updateType_document(type_document: TextFieldValue) {
        this.type_document = type_document
    }

    fun updateDocument_identity(document_identity: TextFieldValue) {
        this.document_identity = document_identity
    }

    fun updateCountry(country: TextFieldValue) {
        this.country = country
    }

    fun updateMobile(mobile: TextFieldValue) {
        this.mobile = mobile
    }

    data class Country(override val name: String) : SelectableItem

    fun getCountryCodes(): List<CountryPhoneCode> {
        return listOf(
            CountryPhoneCode("Select Contry", "", null, "EMPTY"),
            CountryPhoneCode("Perú", "+51", R.drawable.peru, "PE/PER"),
            CountryPhoneCode("Costa Rica", "+506", R.drawable.costa_rica, "CR/CRC"),
        )
    }
}