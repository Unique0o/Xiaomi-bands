package com.example.logifitappp.viewmodel.views.AdditionalInformation


import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.R
import com.example.logifitappp.data.models.CountryModel
import com.example.logifitappp.data.models.CountryPhoneCode
import com.example.logifitappp.data.models.DocumentTypeModel
import com.example.logifitappp.data.remote.dto.requests.PersonalInformationRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AdditionalInformationViewModel(
   // private val userService: UserService
) : ViewModel() {
    private val _state = MutableStateFlow(AdditionalInformationState())
    val state: StateFlow<AdditionalInformationState> = _state.asStateFlow()

    private fun validateField(fieldName: String, value: Any?) {
        val currentErrors = _state.value.formErrors.toMutableMap()

        val error = when (fieldName) {
            "name" -> if ((value as String).isBlank()) "El nombre es requerido" else null
            "lastnames" -> if ((value as String).isBlank()) "Los apellidos son requeridos" else null
            "documentIdentity" -> if ((value as String).isBlank()) "El documento de identidad es requerido" else null
            "mobile" -> when {
                (value as String).isBlank() -> "El número móvil es requerido"
                !isValidPhoneNumber(value) -> "Número móvil inválido"
                else -> null
            }
            "country" -> if (value == null) "El país es requerido" else null
            "documentType" -> if (value == null) "El tipo de documento es requerido" else null
            else -> null
        }

        if (error != null) {
            currentErrors[fieldName] = error
        } else {
            currentErrors.remove(fieldName)
        }

        _state.update { it.copy(formErrors = currentErrors) }
    }
    private fun isValidPhoneNumber(phone: String): Boolean {
        return phone.length >= 8 && phone.all { it.isDigit() }
    }

    fun updateName(name: TextFieldValue) {
        _state.update { it.copy(name = name) }
        validateField("name", name.text)
    }

    fun updateLastnames(lastnames: TextFieldValue) {
        _state.update { it.copy(lastnames = lastnames) }
        validateField("lastnames", lastnames.text)
    }

        fun updateDocumentIdentity(documentIdentity: TextFieldValue) {
        _state.update { it.copy(documentIdentity = documentIdentity) }
        validateField("documentIdentity", documentIdentity.text)
    }

    fun updateMobile(mobile: TextFieldValue) {
        _state.update { it.copy(mobile = mobile) }
        validateField("mobile", mobile.text)
    }

    fun selectCountry(country: CountryModel) {
        _state.update { it.copy(selectedCountry = country) }
        validateField("country", country)
    }

    fun selectDocumentType(documentType: DocumentTypeModel) {
        _state.update { it.copy(selectedDocumentType = documentType) }
        validateField("documentType", documentType)
    }
    fun getCountryCodes(): List<CountryPhoneCode> {
                return listOf(
            CountryPhoneCode("Seleccionar País", "", null, "EMPTY"),
            CountryPhoneCode("Perú", "+51", R.drawable.peru, "PE/PER"),
            CountryPhoneCode("Costa Rica", "+506", R.drawable.costa_rica, "CR/CRC")
        )
    }

    fun validateForm(): Boolean {
        with(_state.value) {
            validateField("name", name.text)
            validateField("lastnames", lastnames.text)
            validateField("documentIdentity", documentIdentity.text)
            validateField("mobile", mobile.text)
            validateField("country", selectedCountry)
            validateField("documentType", selectedDocumentType)
        }

        return _state.value.formErrors.isEmpty()
    }

    fun onSubmitPersonalInformation(onSuccess: () -> Unit) {
        if (!validateForm()) return

        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                val request = PersonalInformationRequest(
                    first_name = _state.value.name.text,
                    last_name = _state.value.lastnames.text,
                    dni = _state.value.documentIdentity.text,
                    phone = _state.value.mobile.text,
                    country_id = _state.value.selectedCountry?.id ?: 0,
                    document_type_id = _state.value.selectedDocumentType?.id ?: 0,
                    date_birth = null,
                    departament_id = null,
                    email = null,
                    profile = null,
                    province_id = null
                )

               // userService.storePersonalInformation(request)
                _state.update { it.copy(isLoading = false) }
                onSuccess()
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error desconocido"
                    )
                }
            }
        }
    }
}
