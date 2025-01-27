package com.example.logifitappp.viewmodel.views


import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.data.models.CountryModel
import com.example.logifitappp.data.models.DocumentTypeModel
import com.example.logifitappp.domain.usecase.GetPersonalInfoUseCase
import com.example.logifitappp.viewmodel.states.AdditionalInformationState
import com.example.logifitappp.viewmodel.views.PersonalInfo.PersonalInfoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalInformationViewModel @Inject constructor (
    private val getPersonalInfoUseCase: GetPersonalInfoUseCase
) : ViewModel() {
    private val _personalInfo = MutableStateFlow<PersonalInfoUiState>(PersonalInfoUiState.Loading)
    val personalInfo: StateFlow<PersonalInfoUiState> = _personalInfo.asStateFlow()

    var state by mutableStateOf(AdditionalInformationState())
        private set

    init {
        viewModelScope.launch {
            _personalInfo.value = PersonalInfoUiState.Loading
            try {
                val info = getPersonalInfoUseCase()
                _personalInfo.value = PersonalInfoUiState.Success(info)
            } catch (e: Exception) {
                _personalInfo.value = PersonalInfoUiState.Error("Failed to load personal info")
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

    fun onItemClick() {
        // TODO
    }

    fun onPhotoClick() {
    }
}


