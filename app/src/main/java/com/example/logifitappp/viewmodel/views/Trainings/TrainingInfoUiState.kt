package com.example.logifitappp.viewmodel.views.Trainings

import com.example.logifitappp.data.models.LessonModel
import com.example.logifitappp.data.models.TrainingInfoModel

sealed class TrainingInfoUiState {
    object Loading : TrainingInfoUiState()
    data class Success(val data: List<TrainingInfoModel>) : TrainingInfoUiState()
    data class Error(val message: String) : TrainingInfoUiState()
}

sealed class TrainingDetailUiState {
    object Loading : TrainingDetailUiState()
    data class Success(val data: List<LessonModel>) : TrainingDetailUiState()
    data class Error(val message: String) : TrainingDetailUiState()
}