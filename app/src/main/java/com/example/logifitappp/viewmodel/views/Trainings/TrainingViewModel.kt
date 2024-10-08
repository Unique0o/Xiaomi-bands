package com.example.logifitappp.viewmodel.views.Trainings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.domain.usecase.GetTrainingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor (private val TrainingUseCase: GetTrainingUseCase) : ViewModel() {
    private val _trainingInfo = MutableStateFlow<TrainingInfoUiState>(TrainingInfoUiState.Loading)
    val trainingInfo: StateFlow<TrainingInfoUiState> = _trainingInfo.asStateFlow()

    init {
        viewModelScope.launch {
            _trainingInfo.value = TrainingInfoUiState.Loading
            try {
                val info = TrainingUseCase()
                _trainingInfo.value = TrainingInfoUiState.Success(info)
            } catch (e: Exception) {
                _trainingInfo.value = TrainingInfoUiState.Error("Failed to load training info")
            }
        }
    }

    fun onTrainingClick(){
        /* to do */
    }
}
