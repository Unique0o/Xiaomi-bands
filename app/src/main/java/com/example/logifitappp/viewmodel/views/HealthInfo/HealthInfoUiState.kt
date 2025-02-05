package com.example.logifitappp.viewmodel.views.HealthInfo

import com.example.logifitappp.data.models.HealthInfoModel


sealed class HealthInfoUiState {
    object Loading : HealthInfoUiState()
    data class Success(val data: List<HealthInfoModel>) : HealthInfoUiState()
    data class Error(val message: String) : HealthInfoUiState()
}
