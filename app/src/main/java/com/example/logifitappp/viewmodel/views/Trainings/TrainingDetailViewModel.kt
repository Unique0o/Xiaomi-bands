package com.example.logifitappp.viewmodel.views.Trainings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.data.models.LessonModel
import com.example.logifitappp.data.models.TrainingInfoModel
import com.example.logifitappp.domain.usecase.GetTrainingLessonsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrainingDetailViewModel @Inject constructor(
    private val getTrainingLessonsUseCase: GetTrainingLessonsUseCase
) : ViewModel() {

    private val _trainingState = MutableStateFlow<TrainingInfoModel?>(null)
    val trainingState: StateFlow<TrainingInfoModel?> = _trainingState

    private val _lessonsState = MutableStateFlow<List<LessonModel>>(emptyList())
    val lessonsState: StateFlow<List<LessonModel>> = _lessonsState

    fun loadTrainingDetails(trainingId: String) {
        viewModelScope.launch {
            _lessonsState.value = getTrainingLessonsUseCase(trainingId)
        }
    }
}