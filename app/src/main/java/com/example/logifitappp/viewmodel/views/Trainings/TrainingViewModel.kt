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
class TrainingViewModel @Inject constructor(
    private val getTrainingsUseCase: GetTrainingUseCase
) : ViewModel() {
    private val _trainingInfo = MutableStateFlow<TrainingInfoUiState>(TrainingInfoUiState.Loading)
    val trainingInfo: StateFlow<TrainingInfoUiState> = _trainingInfo.asStateFlow()

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent.asStateFlow()

    init {
        loadTrainings()
    }

    private fun loadTrainings() {
        viewModelScope.launch {
            _trainingInfo.value = TrainingInfoUiState.Loading
            try {
                val trainings = getTrainingsUseCase()
                _trainingInfo.value = TrainingInfoUiState.Success(trainings)
            } catch (e: Exception) {
                _trainingInfo.value = TrainingInfoUiState.Error("Failed to load trainings")
            }
        }
    }

    fun onTrainingClick(trainingId: String) {
        _navigationEvent.value = NavigationEvent.NavigateToTrainingDetail(trainingId)
    }

    fun onNavigationEventConsumed() {
        _navigationEvent.value = null
    }
}

sealed class NavigationEvent {
    data class NavigateToTrainingDetail(val trainingId: String) : NavigationEvent()
}