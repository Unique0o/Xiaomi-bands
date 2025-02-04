package com.example.logifitappp.viewmodel.views


import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.AppPreferences
import com.example.logifitappp.core.utils.toFile
import com.example.logifitappp.data.models.CountryModel
import com.example.logifitappp.data.models.DepartmentModel
import com.example.logifitappp.data.models.DocumentTypeModel
import com.example.logifitappp.data.models.ProvinceModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.data.remote.dto.requests.StorePersonalInformationRequest
import com.example.logifitappp.data.remote.dto.response.toUser
import com.example.logifitappp.domain.service.UserService
import com.example.logifitappp.domain.usecase.UpdatePersonalInformationFormDataUseCase
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.example.logifitappp.viewmodel.states.AdditionalInformationState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException


@HiltViewModel(assistedFactory = PersonalInformationViewModel.PersonalInformationViewModelFactory::class)
class PersonalInformationViewModel @AssistedInject constructor(
    @Assisted private val user: UserModel?,
    private val updatePersonalInformationFormDataUseCase: UpdatePersonalInformationFormDataUseCase,
    private val userService: UserService
) : ViewModel() {
    @AssistedFactory
    interface PersonalInformationViewModelFactory {
        fun create(user: UserModel?): PersonalInformationViewModel
    }

    var state by mutableStateOf(AdditionalInformationState())
        private set

    init {
        fetchNecessaryData()
    }

    fun fetchNecessaryData() {
        viewModelScope.launch {
            try {
                state = state.copy(
                    isNecessaryDataFetching = true,
                    storingInformationStatus = AppStatusCodeEnum.FETCHING_ADDITIONAL_INFORMATION
                )

                updatePersonalInformationFormDataUseCase()

                val countries = App.database.countryDao().all()
                val documentTypes = App.database.documentTypeDao().all()
                val selectedCountry =
                    user?.countryId?.let { countries.find { country -> country.id == it } }
                val selectedDocumentType =
                    user?.documentId?.let { documentTypes.find { documentType -> documentType.id == it } }

                val departments =
                    App.database.departmentDao().all(user?.countryId ?: countries[0].id)
                val selectedDepartment =
                    user?.departmentId?.let { departments.find { departments -> departments.id == it } }

                val provinces =
                    App.database.provinceDao().all(user?.departmentId ?: departments[0].id)
                val selectedProvince =
                    user?.provinceId?.let { provinces.find { province-> province.id == it } }


                state = state.copy(
                    countries = countries,
                    documentTypes = documentTypes,
                    departments = departments,
                    provinces = provinces,
                    hasNecessaryDataFetchingFailed = false,
                    identityDocument = TextFieldValue(user?.identificationDocument ?: ""),
                    names = TextFieldValue(user?.firstName ?: ""),
                    phone = TextFieldValue(user?.phone ?: ""),
                    photo = user?.profilePhoto,
                    selectedCountry = selectedCountry,
                    selectedDocumentType = selectedDocumentType,
                    selectedDepartment = selectedDepartment,
                    selectedProvince = selectedProvince,
                    surnames = TextFieldValue(user?.lastName ?: ""),
                    birthdate = user?.birthDate ?: "",
                    email = TextFieldValue(user?.email ?: "")
                )

            } catch (e: Exception) {
                state = state.copy(hasNecessaryDataFetchingFailed = true)
            } finally {
                state = state.copy(isNecessaryDataFetching = false)
            }
        }
    }

    fun stopProcessing() {
        state = state.copy(isInformationStoring = false)
    }

    private fun storePersonalInformation() {
        if (user == null) return

        if (!validateInformationForm()) {
            stopProcessing()
            return
        }

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
                        phone = state.phone.text,
                        email = state.email.text,
                        date_birth = state.birthdate,
                        departament_id = state.selectedDepartment?.id,
                        province_id = state.selectedProvince?.id
                    )
                )

                App.database.userDao().store(
                    user.copy(
                        countryId = state.selectedCountry?.id,
                        documentId = state.selectedDocumentType?.id,
                        identificationDocument = state.identityDocument.text,
                        firstName = state.names.text,
                        lastName = state.surnames.text,
                        phone = state.phone.text,
                        email = state.email.text,
                        birthDate = state.birthdate,
                        departmentId = state.selectedDepartment?.id,
                        provinceId = state.selectedProvince?.id
                    )
                )

                App.signalReloadAuthenticatedUser()

                state = state.copy(
                    currentPage = 1,
                    isInformationStoring = false
                )
                Toast.makeText(App.context, "Profile Updated!", Toast.LENGTH_SHORT).show()
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

                    else -> Thread.sleep(100)
                }

                App.preferences
                    .getPreferences()
                    .edit()
                    .putBoolean(AppPreferences.OMIT_ADDITIONAL_INFORMATION, true)
                    .apply()

                storePersonalInformation()
//                state = state.copy(isInformationStoring = false)

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
        state = state.copy(
            selectedCountry = country,
            departments = App.database.departmentDao().all(country.id).toList(),
            selectedDepartment = null
        )
    }

    fun updateDepartment(department: DepartmentModel) {
        state = state.copy(
            selectedDepartment = department,
            provinces = App.database.provinceDao().all(department.id).toList(),
            selectedProvince = null
        )

    }

    fun updateProvince(province: ProvinceModel) {
        state = state.copy(selectedProvince = province)
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

    fun updateEmail(email: TextFieldValue) {
        state = state.copy(email = email)
    }

    fun updateBirthdate(birthdate: String) {
        state = state.copy(birthdate = birthdate)
    }

    fun updateBirthdateError(birthdate: String) {
        state = state.copy(birthdateError = birthdate)
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
        val isEmailValid =
            state.email.text.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(state.email.text)
                .matches()
        val isDocumentTypeValid = state.selectedDocumentType != null
        val isIdentityDocumentValid = state.identityDocument.text.isNotBlank()
        val isCountryValid = state.selectedCountry != null
        val isDepartmentValid = state.selectedDepartment != null
        val isProvinceValid = state.selectedProvince != null
        val isPhoneValid = Patterns.PHONE.matcher(state.phone.text).matches()
        val isBirthdateValid =
            state.birthdate.isNotEmpty() && state.birthdate != App.context.getString(R.string.birthdate)

        state = state.copy(
            namesError = if (isNamesValid) null else App.context.getString(R.string.names_validation_error_message),
            surnamesError = if (isSurnamesValid) null else App.context.getString(R.string.surnames_validation_error_message),
            emailError = if (isEmailValid) null else App.context.getString(R.string.email_validation_error_message),
            identityDocumentError = if (isIdentityDocumentValid) null else App.context.getString(R.string.identity_document_validation_error_message),
            phoneError = if (isPhoneValid) null else App.context.getString(R.string.phone_validation_error_message),
            birthdateError = if (isBirthdateValid) null else App.context.getString(R.string.bday_type_validation_error_message),
            documentTypesError = if (isDocumentTypeValid) null else App.context.getString(R.string.document_type_validation_error_message),
            countriesError = if (isCountryValid) null else App.context.getString(R.string.country_validation_error_message),
            departmentTypesError = if (isDepartmentValid) null else App.context.getString(R.string.department_validation_error_message),
            provinceError = if (isProvinceValid) null else App.context.getString(R.string.province_validation_error_message)
        )

        return isNamesValid && isSurnamesValid && isDocumentTypeValid && isIdentityDocumentValid &&
                isEmailValid && isCountryValid && isPhoneValid && isBirthdateValid && isDepartmentValid &&
                isProvinceValid
    }

    private fun validateProfilePhoto(): Boolean {
        val isProfilePhotoValid = state.photo != null

        state = state.copy(
            phoneError = if (isProfilePhotoValid) null else App.context.getString(R.string.profile_photo_additional_information_error_message)
        )

        return isProfilePhotoValid
    }
}


