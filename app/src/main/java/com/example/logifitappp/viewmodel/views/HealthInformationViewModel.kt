package com.example.logifitappp.viewmodel.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.domain.usecase.HealthInfoUseCase
import com.example.logifitappp.viewmodel.views.HealthInfo.HealthInfoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthInformationViewModel @Inject constructor (private val HealthInfoUseCase: HealthInfoUseCase) : ViewModel() {
    private val _healthInfo = MutableStateFlow<HealthInfoUiState>(HealthInfoUiState.Loading)
    val healthInfo: StateFlow<HealthInfoUiState> = _healthInfo.asStateFlow()

    init {
        viewModelScope.launch {
            _healthInfo.value = HealthInfoUiState.Loading
            try {
                val info = HealthInfoUseCase()
                _healthInfo.value = HealthInfoUiState.Success(info)
            } catch (e: Exception) {
                _healthInfo.value = HealthInfoUiState.Error("Failed to load health info")
            }
        }
    }

    fun onEditClick() {
        /* to do */
    }
}
