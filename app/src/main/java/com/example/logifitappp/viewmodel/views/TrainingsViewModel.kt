package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.core.App
import com.example.logifitappp.core.AppPreferences
import com.example.logifitappp.data.remote.dto.response.FetchTrainingResponse
import com.example.logifitappp.domain.service.TrainingService
import com.example.logifitappp.viewmodel.states.TrainingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingsViewModel @Inject constructor(
    private val trainingService: TrainingService
): ViewModel() {
    var state by mutableStateOf(TrainingsState())
        private set

    var trainings = mutableListOf<FetchTrainingResponse>()
        private set

    init {
        fetchTrainings()
    }

    fun fetchTrainings() {
        viewModelScope.launch {
            try {
                state = state.copy(isLoading = true)

                trainings.addAll(trainingService.all())

                state = state.copy(hasFetchTrainingsFailed = false)
            } catch (e: Exception) {
                state = state.copy(hasFetchTrainingsFailed = true)
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }

    fun updateProgress() {
        println("update progress trainings")

        val completedTrainingId = App.preferences.getInt(AppPreferences.COMPLETED_TRAINING, -1)
        val inProgressTrainingId = App.preferences.getInt(AppPreferences.IN_PROGRESS_TRAINING, -1)

        println("completed training id: $completedTrainingId")
        println("in progress training id: $inProgressTrainingId")

        val completedTraining = trainings.find { it.id == completedTrainingId }
        val inProgressTraining = trainings.find { it.id == inProgressTrainingId }

        if (completedTraining != null) completedTraining.progressPercentage = 100f

        if (inProgressTraining != null && inProgressTrainingId != completedTrainingId) inProgressTraining.progressPercentage = 10f

        App.preferences
            .getPreferences()
            .edit()
            .remove(AppPreferences.COMPLETED_TRAINING)
            .remove(AppPreferences.IN_PROGRESS_TRAINING)
            .apply()
    }
}