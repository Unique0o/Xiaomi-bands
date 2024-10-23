package com.example.logifitappp.viewmodel.views.PersonalInfo


import com.example.logifitappp.data.models.PersonalInfoModel

sealed class PersonalInfoUiState {
    object Loading : PersonalInfoUiState()
    data class Success(val data: List<PersonalInfoModel>) : PersonalInfoUiState()
    data class Error(val message: String) : PersonalInfoUiState()
}