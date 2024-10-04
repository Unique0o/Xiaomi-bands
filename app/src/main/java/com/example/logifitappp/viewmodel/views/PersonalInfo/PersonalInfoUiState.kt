package com.example.logifitappp.viewmodel.views.PersonalInfo


import com.example.logifitappp.data.models.PersonalInfoItem

sealed class PersonalInfoUiState {
    object Loading : PersonalInfoUiState()
    data class Success(val data: List<PersonalInfoItem>) : PersonalInfoUiState()
    data class Error(val message: String) : PersonalInfoUiState()
}