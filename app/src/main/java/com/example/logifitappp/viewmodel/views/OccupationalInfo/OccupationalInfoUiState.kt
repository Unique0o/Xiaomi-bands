package com.example.logifitappp.viewmodel.views.OccupationalInfo

import com.example.logifitappp.data.models.OccupationalInfoItem

sealed class OccupationalInfoUiState {
    object Loading : OccupationalInfoUiState()
    data class Success(val data: List<OccupationalInfoItem>) : OccupationalInfoUiState()
    data class Error(val message: String) : OccupationalInfoUiState()
}