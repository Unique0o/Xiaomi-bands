package com.example.logifitappp.viewmodel.views.AdditionalInformation


import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.R
import com.example.logifitappp.data.models.CountryModel
import com.example.logifitappp.data.models.CountryPhoneCode
import com.example.logifitappp.data.models.DocumentTypeModel
import com.example.logifitappp.data.remote.dto.requests.StorePersonalInformationRequest
import com.example.logifitappp.domain.repository.CountryRepository
import com.example.logifitappp.domain.repository.UserRepository
import com.example.logifitappp.domain.service.CountryService
import com.example.logifitappp.domain.service.DocumentTypeService
import com.example.logifitappp.domain.service.UserService
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class AdditionalInformationViewModel @Inject constructor(
    private val countryService: CountryService,
    private val documentTypeService: DocumentTypeService,
    private val countryRepository: CountryRepository,
    private val userService: UserService,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AdditionalInformationState())
    val state: StateFlow<AdditionalInformationState> = _state.asStateFlow()

    init {
        fetchNecessaryData()
    }

    private fun fetchNecessaryData() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }

                withContext(Dispatchers.IO) {
                    launch { countryService.update() }
                    launch {
                        val response = documentTypeService.all()
                        _state.update { currentState ->
                            currentState.copy(
                                documentTypes = response.documents.map { document ->
                                    DocumentTypeModel(
                                        externalIdentifier = document.id,
                                        name = document.name
                                    )
                                }
                            )
                        }
                    }
                }

                val countries = countryRepository.all()
                val currentUser = userRepository.getLoggedIn()

                _state.update { currentState ->
                    currentState.copy(
                        countries = countries,
                        documentIdentity = TextFieldValue(currentUser?.identificationDocument ?: ""),
                        name = TextFieldValue(currentUser?.firstName ?: ""),
                        mobile = TextFieldValue(currentUser?.phone ?: ""),
                       // photoUri = currentUser?.profilePhoto,
                        selectedCountry = countries.find {
                            it.externalIdentifier == currentUser?.countryId
                        },
                        selectedDocumentType = currentState.documentTypes.find {
                            it.externalIdentifier == currentUser?.documentId
                        },
                        lastnames = TextFieldValue(currentUser?.lastName ?: ""),
                        isLoading = false
                    )
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = when (e) {
                            is HttpConsumerException -> e.message
                            else -> "Error desconocido"
                        }
                    )
                }

                try {
                    val countries = countryRepository.all()
                    _state.update { currentState ->
                        currentState.copy(countries = countries)
                    }
                } catch (e: Exception) {
                    _state.update { it.copy(error = e.message ?: "Error desconocido") }
                }
            }
        }
    }

    fun updateName(name: TextFieldValue) {
        _state.update {
            it.copy(
                name = name,
                formErrors = it.formErrors - "names"
            )
        }
    }

    fun updateLastnames(lastnames: TextFieldValue) {
        _state.update {
            it.copy(
                lastnames = lastnames,
                formErrors = it.formErrors - "surnames"
            )
        }
    }

    fun updateDocumentIdentity(documentIdentity: TextFieldValue) {
        _state.update {
            it.copy(
                documentIdentity = documentIdentity,
                formErrors = it.formErrors - "dni"
            )
        }
    }

    fun updateMobile(mobile: TextFieldValue) {
        _state.update {
            it.copy(
                mobile = mobile,
                formErrors = it.formErrors - "phone"
            )
        }
    }

    fun selectCountry(country: CountryModel) {
        _state.update {
            it.copy(
                selectedCountry = country,
                formErrors = it.formErrors - "country"
            )
        }
    }

    fun selectDocumentType(documentType: DocumentTypeModel) {
        _state.update {
            it.copy(
                selectedDocumentType = documentType,
                formErrors = it.formErrors - "documentType"
            )
        }
    }

    fun getCountryCodes(): List<CountryPhoneCode> = listOf(
        CountryPhoneCode(
            name = "Seleccionar País",
            code = "",
            flagResId = null,
            id = "EMPTY"
        ),
        CountryPhoneCode(
            name = "Perú",
            code = "+51",
            flagResId = R.drawable.peru,
            id = "PE/PER"
        ),
        CountryPhoneCode(
            name = "Costa Rica",
            code = "+506",
            flagResId = R.drawable.costa_rica,
            id = "CR/CRC"
        )
    )

    private fun validateFields(state: AdditionalInformationState): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (state.name.text.isBlank()) {
            errors["names"] = "El nombre es requerido"
        }
        if (state.lastnames.text.isBlank()) {
            errors["surnames"] = "Los apellidos son requeridos"
        }
        if (state.selectedDocumentType == null) {
            errors["documentType"] = "El tipo de documento es requerido"
        }
        if (state.documentIdentity.text.isBlank()) {
            errors["dni"] = "El documento es requerido"
        }
        if (state.selectedCountry == null) {
            errors["country"] = "El país es requerido"
        }
        if (state.mobile.text.isBlank()) {
            errors["phone"] = "El teléfono es requerido"
        }

        return errors
    }

    fun onSubmitPersonalInformation(onSuccess: () -> Unit) {
        val currentState = _state.value
        val errors = validateFields(currentState)

        if (errors.isNotEmpty()) {
            _state.update { it.copy(formErrors = errors) }
            return
        }

        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }

                val currentUser = userRepository.getLoggedIn()
                    ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

                val request = StorePersonalInformationRequest(
                    country_id = currentState.selectedCountry?.externalIdentifier,
                    date_birth = null,
                    departament_id = null,
                    dni = currentState.documentIdentity.text,
                    document_type_id = currentState.selectedDocumentType?.externalIdentifier,
                    email = null,
                    first_name = currentState.name.text,
                    last_name = currentState.lastnames.text,
                    phone = currentState.mobile.text,
                    profile = null,
                    province_id = null
                )

                /*currentUser.external_identifier?.let {
                    userService.storePersonalInformation(
                        userId = it,
                        request = request
                    )
                    userRepository.createOrUpdate(
                        currentUser.copy(
                            countryId = request.country_id,
                            documentId = request.document_type_id,
                            identificationDocument = request.dni,
                            firstName = request.first_name,
                            lastName = request.last_name,
                            phone = request.phone
                        )
                    )
                }*/

                _state.update { it.copy(isLoading = false) }
                onSuccess()
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = when (e) {
                            is HttpConsumerException -> e.message
                            else -> "Error desconocido"
                        }
                    )
                }
            }
        }
    }
}