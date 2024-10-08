package com.example.logifitappp.viewmodel.views.PersonalInfo


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.domain.usecase.GetPersonalInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalInfoViewModel @Inject constructor (private val getPersonalInfoUseCase: GetPersonalInfoUseCase) : ViewModel() {
    private val _personalInfo = MutableStateFlow<PersonalInfoUiState>(PersonalInfoUiState.Loading)
    val personalInfo: StateFlow<PersonalInfoUiState> = _personalInfo.asStateFlow()

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

    fun onItemClick() {
        // TODO
    }

    fun onPhotoClick() {
       // TODO
    }
}


