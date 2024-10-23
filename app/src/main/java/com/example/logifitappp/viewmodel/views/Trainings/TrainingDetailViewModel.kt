package com.example.logifitappp.viewmodel.views.Trainings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.data.models.TrainingInfoModel
import com.example.logifitappp.domain.usecase.GetTrainingLessonsUseCase
import com.example.logifitappp.domain.usecase.GetTrainingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TrainingDetailViewModel @Inject constructor(
    private val getTrainingUseCase: GetTrainingUseCase,
    private val getTrainingLessonsUseCase: GetTrainingLessonsUseCase
) : ViewModel() {

    private val _allTrainingsState = MutableStateFlow<TrainingInfoUiState>(TrainingInfoUiState.Loading)

    private val _selectedTraining = MutableStateFlow<TrainingInfoModel?>(null)
    val selectedTraining: StateFlow<TrainingInfoModel?> = _selectedTraining.asStateFlow()

    private val _lessonsState = MutableStateFlow<TrainingDetailUiState>(TrainingDetailUiState.Loading)
    val lessonsState: StateFlow<TrainingDetailUiState> = _lessonsState.asStateFlow()

    init {
        loadAllTrainings()
    }

    private fun loadAllTrainings() {
        viewModelScope.launch {
            _allTrainingsState.value = TrainingInfoUiState.Loading
            try {
                val trainings = getTrainingUseCase()
                _allTrainingsState.value = TrainingInfoUiState.Success(trainings)
            } catch (e: Exception) {
                _allTrainingsState.value = TrainingInfoUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun loadTrainingDetails(trainingId: String) {
        viewModelScope.launch {
            when (val state = _allTrainingsState.value) {
                is TrainingInfoUiState.Success -> {
                    _selectedTraining.value = state.data.find { it.id == trainingId }
                    if (_selectedTraining.value != null) {
                        loadLessons(trainingId)
                    } else {
                        _lessonsState.value = TrainingDetailUiState.Error("Training not found")
                    }
                }
                is TrainingInfoUiState.Error -> {
                    _lessonsState.value = TrainingDetailUiState.Error("Failed to load training info")
                }
                TrainingInfoUiState.Loading -> {
                    _lessonsState.value = TrainingDetailUiState.Loading
                }
            }
        }
    }

    private suspend fun loadLessons(trainingId: String) {
        _lessonsState.value = TrainingDetailUiState.Loading
        try {
            val lessons = getTrainingLessonsUseCase(trainingId)
            _lessonsState.value = TrainingDetailUiState.Success(lessons)
        } catch (e: Exception) {
            _lessonsState.value = TrainingDetailUiState.Error(e.message ?: "Unknown error occurred")
        }
    }
}