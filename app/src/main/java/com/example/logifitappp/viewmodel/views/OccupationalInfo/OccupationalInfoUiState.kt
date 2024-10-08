package com.example.logifitappp.viewmodel.views.OccupationalInfo

import com.example.logifitappp.data.models.OccupationalInfoItemModel

sealed class OccupationalInfoUiState {
    object Loading : OccupationalInfoUiState()
    data class Success(val data: List<OccupationalInfoItemModel>) : OccupationalInfoUiState()
    data class Error(val message: String) : OccupationalInfoUiState()
}