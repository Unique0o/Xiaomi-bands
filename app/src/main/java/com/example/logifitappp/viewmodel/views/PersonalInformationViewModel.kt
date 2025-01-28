package com.example.logifitappp.viewmodel.views


import android.net.Uri
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.App.Companion.context
import com.example.logifitappp.core.AppPreferences
import com.example.logifitappp.core.utils.toFile
import com.example.logifitappp.data.models.CountryModel
import com.example.logifitappp.data.models.DocumentTypeModel
import com.example.logifitappp.data.models.PersonalInfoModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.data.remote.dto.requests.StorePersonalInformationRequest
import com.example.logifitappp.data.remote.dto.response.toUser
import com.example.logifitappp.domain.service.UserService
import com.example.logifitappp.domain.usecase.GetPersonalInfoUseCase
import com.example.logifitappp.domain.usecase.UpdatePersonalInformationFormDataUseCase
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.example.logifitappp.viewmodel.states.AdditionalInformationState
import com.example.logifitappp.viewmodel.views.PersonalInfo.PersonalInfoUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel(assistedFactory = PersonalInformationViewModel.PersonalInformationViewModelFactory::class)
class PersonalInformationViewModel @AssistedInject constructor(
    @Assisted private val user: UserModel?,
    private val updatePersonalInformationFormDataUseCase: UpdatePersonalInformationFormDataUseCase,
    private val getPersonalInfoUseCase: GetPersonalInfoUseCase,
    private val userService: UserService
) : ViewModel() {
    @AssistedFactory
    interface PersonalInformationViewModelFactory {
        fun create(user: UserModel?): PersonalInformationViewModel
    }

    var state by mutableStateOf(AdditionalInformationState())
        private set

    private val _personalInfo = MutableStateFlow<PersonalInfoUiState>(PersonalInfoUiState.Loading)
    val personalInfo: StateFlow<PersonalInfoUiState> = _personalInfo.asStateFlow()


    init {
        fetchNecessaryData()
    }

    fun fetchNecessaryData() {
        viewModelScope.launch {
            try {
                state = state.copy(isNecessaryDataFetching = true)

                updatePersonalInformationFormDataUseCase()

                val countries = App.database.countryDao().all()
                val documentTypes = App.database.documentTypeDao().all()

                state = state.copy(
                    countries = countries,
                    documentTypes = documentTypes,
                    hasNecessaryDataFetchingFailed = false,
                    identityDocument = TextFieldValue(user?.identificationDocument ?: ""),
                    names = TextFieldValue(user?.firstName ?: ""),
                    phone = TextFieldValue(user?.phone ?: ""),
                    photo = user?.profilePhoto,
                    selectedCountry = user?.countryId?.let { countries.find { country -> country.id == it } },
                    selectedDocumentType = user?.documentId?.let { documentTypes.find { documentType -> documentType.id == it } },
                    surnames = TextFieldValue(user?.lastName ?: "")
                )

                setModelValues()
            } catch (e: Exception) {
                state = state.copy(hasNecessaryDataFetchingFailed = true)
            } finally {
                state = state.copy(isNecessaryDataFetching = false)
            }
        }
    }

    private fun setModelValues() {
        viewModelScope.launch {
            _personalInfo.value = PersonalInfoUiState.Loading
            try {
                val info = getPersonalInfoUseCase()
                for (index in info.indices) {
                    if (user != null) {
                        when (info[index].label) {
                            "Names" -> info[index].value = user.firstName ?: ""
                            "Surnames" -> info[index].value = user.lastName ?: ""
                            "Identification Document" -> info[index].value =
                                user.identificationDocument
                                    ?: ""

                            "Birthdate" -> info[index].value = user.birthDate
                                ?: context.getString(R.string.not_selected)

                            "Email" -> info[index].value = user.email ?: ""
                            "Phone" -> info[index].value = user.phone ?: ""
                            "Document type" -> info[index].value = user.documentId?.toString()
                                ?: context.getString(
                                    R.string.not_selected
                                )

                            "Country" -> info[index].value = user.countryId?.toString()
                                ?: context.getString(R.string.not_selected)

                            "Department" -> info[index].value = user.departmentId?.toString()
                                ?: context.getString(R.string.not_selected)

                            "Province" -> info[index].value = user.provinceId?.toString()
                                ?: context.getString(R.string.not_selected)

                            else -> ""
                        }
                    }
                }
                _personalInfo.value = PersonalInfoUiState.Success(info)

            } catch (e: Exception) {
                _personalInfo.value = PersonalInfoUiState.Error("Failed to load personal info")
            }
        }
    }

    fun stopProcessing() {
        state = state.copy(isInformationStoring = false)
    }

    fun storePersonalInformation() {
        if (user == null) return

        if (!validateInformationForm()) return

        viewModelScope.launch {
            try {
                state = state.copy(
                    isInformationStoring = true,
                    storingInformationStatus = AppStatusCodeEnum.STORING_ADDITIONAL_INFORMATION
                )

                userService.storePersonalInformation(
                    user.id, StorePersonalInformationRequest(
                        country_id = state.selectedCountry?.id,
                        dni = state.identityDocument.text,
                        document_type_id = state.selectedDocumentType?.id,
                        first_name = state.names.text,
                        last_name = state.surnames.text,
                        phone = state.phone.text
                    )
                )

                App.database.userDao().store(
                    user.copy(
                        countryId = state.selectedCountry?.id,
                        documentId = state.selectedDocumentType?.id,
                        identificationDocument = state.identityDocument.text,
                        firstName = state.names.text,
                        lastName = state.surnames.text,
                        phone = state.phone.text
                    )
                )

                App.signalReloadAuthenticatedUser()

                state = state.copy(
                    currentPage = 1,
                    isInformationStoring = false
                )
            } catch (e: HttpConsumerException) {
                state = state.copy(storingInformationStatus = e.getStatus())
            }
        }
    }

    fun storeProfilePhoto() {
        if (user == null) return

        if (!validateProfilePhoto()) return

        viewModelScope.launch {
            try {
                state = state.copy(
                    isInformationStoring = true,
                    storingInformationStatus = AppStatusCodeEnum.STORING_PROFILE_PHOTO
                )

                when (val photo = state.photo) {
                    is Uri -> {
                        userService.uploadProfilePhoto(user.id, photo.toFile(App.context))

                        App.database.userDao().store(userService.fetch().toUser(user.accessToken))
                        App.signalReloadAuthenticatedUser()
                    }

                    else -> Thread.sleep(1000)
                }

                App.preferences
                    .getPreferences()
                    .edit()
                    .putBoolean(AppPreferences.OMIT_ADDITIONAL_INFORMATION, true)
                    .apply()

                state = state.copy(isInformationStoring = false)

                App.signalRequestAdditionalInformation(false)
            } catch (e: HttpConsumerException) {
                state = state.copy(storingInformationStatus = e.getStatus())
            } catch (e: IOException) {
                state =
                    state.copy(storingInformationStatus = AppStatusCodeEnum.FAILED_PROFILE_PHOTO_STORE)
            }
        }
    }

    fun updateCountry(country: CountryModel) {
        state = state.copy(selectedCountry = country)
    }

    fun updateDocumentType(documentType: DocumentTypeModel) {
        state = state.copy(selectedDocumentType = documentType)
    }

    fun updateIdentityDocument(identityDocument: TextFieldValue) {
        state = state.copy(identityDocument = identityDocument)
    }

    fun updateNames(names: TextFieldValue) {
        state = state.copy(names = names)
    }

    fun updatePhone(phone: TextFieldValue) {
        state = state.copy(phone = phone)
    }

    fun updateProfilePhoto(uri: Uri) {
        state = state.copy(photo = uri)
    }

    fun updateSurnames(surnames: TextFieldValue) {
        state = state.copy(surnames = surnames)
    }

    private fun validateInformationForm(): Boolean {
        val isNamesValid = state.names.text.isNotBlank()
        val isSurnamesValid = state.surnames.text.isNotBlank()
        val isDocumentTypeValid = state.selectedDocumentType != null
        val isIdentityDocumentValid = state.identityDocument.text.isNotBlank()
        val isCountryValid = state.selectedCountry != null
        val isPhoneValid = Patterns.PHONE.matcher(state.phone.text).matches()

        state = state.copy(
            countriesError = if (isCountryValid) null else App.context.getString(R.string.country_validation_error_message),
            namesError = if (isNamesValid) null else App.context.getString(R.string.names_validation_error_message),
            documentTypesError = if (isDocumentTypeValid) null else App.context.getString(R.string.document_type_validation_error_message),
            identityDocumentError = if (isIdentityDocumentValid) null else App.context.getString(R.string.identity_document_validation_error_message),
            phoneError = if (isPhoneValid) null else App.context.getString(R.string.phone_validation_error_message),
            surnamesError = if (isSurnamesValid) null else App.context.getString(R.string.surnames_validation_error_message)
        )

        return isNamesValid && isSurnamesValid && isDocumentTypeValid && isIdentityDocumentValid && isCountryValid && isPhoneValid
    }

    private fun validateProfilePhoto(): Boolean {
        val isProfilePhotoValid = state.photo != null

        state = state.copy(
            phoneError = if (isProfilePhotoValid) null else App.context.getString(R.string.profile_photo_additional_information_error_message)
        )

        return isProfilePhotoValid
    }
}


