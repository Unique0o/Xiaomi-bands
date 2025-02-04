package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.App.Companion.context
import com.example.logifitappp.data.models.BloodModel
import com.example.logifitappp.data.models.GenderModel
import com.example.logifitappp.data.models.HealthInfoModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.domain.usecase.HealthInfoUseCase
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.example.logifitappp.viewmodel.states.AdditionalInformationState
import com.example.logifitappp.viewmodel.views.HealthInfo.HealthInfoUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = HealthInformationViewModel.HealthInformationViewModelFactory::class)
class HealthInformationViewModel @AssistedInject constructor(
    @Assisted private val user: UserModel?,
    private val healthInfoUseCase: HealthInfoUseCase
) : ViewModel() {
    @AssistedFactory
    interface HealthInformationViewModelFactory {
        fun create(user: UserModel?): HealthInformationViewModel
    }

    var state by mutableStateOf(AdditionalInformationState())
        private set

    private val _healthInfo = MutableStateFlow<HealthInfoUiState>(HealthInfoUiState.Loading)
    val healthInfo: StateFlow<HealthInfoUiState> = _healthInfo.asStateFlow()

    private var selectedBloodType: BloodModel? = null
    private var selectedGender: GenderModel? = null

    init {
        fetchNecessaryData()
    }

    private fun fetchNecessaryData() {
        viewModelScope.launch {
            try {
                state = state.copy(
                    isNecessaryDataFetching = true,
                    storingInformationStatus = AppStatusCodeEnum.FETCHING_ADDITIONAL_INFORMATION
                )

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
                    user?.provinceId?.let { provinces.find { province -> province.id == it } }

                selectedBloodType = getBloodList().find { it.id == user?.bloodType }
                selectedGender = getGenderList().find { it.id == user?.gender }

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
                    email = TextFieldValue(user?.email ?: ""),
                    bloodType = selectedBloodType?.id ?: "",
                    gender = selectedGender?.id ?: "",
                    height = user?.height ?: 0.0F,
                    weight = user?.weight ?: 0.0F
                )
                getUI()
            } catch (e: Exception) {
                state = state.copy(hasNecessaryDataFetchingFailed = true)
            } finally {
                state = state.copy(isNecessaryDataFetching = false)
            }
        }
    }

    private fun getUI() {
        viewModelScope.launch {
            _healthInfo.value = HealthInfoUiState.Loading
            try {
                val info = healthInfoUseCase().toMutableList()
                selectedBloodType?.let { bloodType ->
                    val existingIndex = info.indexOfFirst { it.title == "Blood Type" }
                    if (existingIndex != -1) {
                        info[existingIndex] = HealthInfoModel(
                            context.getString(R.string.blood_type),
                            bloodType.label,
                            "",
                            R.drawable.blood
                        )

                    } else {
                        info.add(
                            HealthInfoModel(
                                context.getString(R.string.blood_type),
                                bloodType.label,
                                "",
                                R.drawable.blood
                            )
                        )
                    }
                }

                // Check if selectedGender is not null, update or add to list
                selectedGender?.let { gender ->
                    val existingIndex = info.indexOfFirst { it.title == "Gender" }
                    if (existingIndex != -1) {
                        info[existingIndex] = HealthInfoModel(
                            context.getString(R.string.gender),
                            gender.label,
                            "",
                            R.drawable.geneder
                        )
                    } else {
                        info.add(
                            HealthInfoModel(
                                context.getString(R.string.gender),
                                gender.label,
                                "",
                                R.drawable.geneder
                            )
                        )
                    }
                }

                _healthInfo.value = HealthInfoUiState.Success(info)
            } catch (e: Exception) {
                _healthInfo.value = HealthInfoUiState.Error("Failed to load health info")
            }
        }
    }

    fun updateSelectedValue(isBlood: Boolean, selectedItem: Any) {
        if (isBlood && selectedItem is BloodModel) {
            selectedBloodType = selectedItem
            state = state.copy(bloodType = selectedItem.id)
        } else if (!isBlood && selectedItem is GenderModel) {
            selectedGender = selectedItem
            state = state.copy(gender = selectedItem.id)
        }
        updateHealthInfo()
    }

    private fun updateHealthInfo() {

        val updatedList = _healthInfo.value.let {
            if (it is HealthInfoUiState.Success) it.data.toMutableList() else mutableListOf()
        }

        updatedList.removeAll {
            it.title == context.getString(R.string.blood_type) || it.title == context.getString(
                R.string.gender
            )
        }

        updatedList.add(
            HealthInfoModel(
                context.getString(R.string.blood_type),
                selectedBloodType?.label ?: "",
                "",
                R.drawable.blood
            )
        )

        updatedList.add(
            HealthInfoModel(
                context.getString(R.string.gender),
                selectedGender?.label ?: "",
                "",
                R.drawable.geneder
            )
        )

        _healthInfo.value = HealthInfoUiState.Success(updatedList)
    }


    private fun storePersonalInformation() {
        if (user == null) return

        viewModelScope.launch {
            try {
                state = state.copy(
                    isInformationStoring = true,
                    storingInformationStatus = AppStatusCodeEnum.STORING_ADDITIONAL_INFORMATION
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
            } catch (e: HttpConsumerException) {
                state = state.copy(storingInformationStatus = e.getStatus())
            }
        }
    }


    fun getBloodList(): List<BloodModel> {
        val bloodTypes = listOf(
            BloodModel("A+", "A+"),
            BloodModel("A-", "A-"),
            BloodModel("B+", "B+"),
            BloodModel("B-", "B-"),
            BloodModel("AB+", "AB+"),
            BloodModel("AB-", "AB-"),
            BloodModel("O+", "O+"),
            BloodModel("O-", "O-")
        )
        return bloodTypes
    }

    fun getGenderList(): List<GenderModel> {
        val genders = listOf(
            GenderModel("M", "Male"),
            GenderModel("F", "Female"),
        )
        return genders
    }
}
