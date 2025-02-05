package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.core.App
import com.example.logifitappp.core.AppPreferences
import com.example.logifitappp.data.remote.dto.response.FetchLessonResponse
import com.example.logifitappp.domain.service.LessonService
import com.example.logifitappp.domain.service.TrainingService
import com.example.logifitappp.domain.usecase.ShareTrainingCertificateUseCase
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.example.logifitappp.viewmodel.states.TrainingDetailState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = TrainingDetailViewModel.TrainingDetailViewModelFactory::class)
class TrainingDetailViewModel @AssistedInject constructor(
    @Assisted private val trainingId: Int,
    private val lessonService: LessonService,
    private val shareTrainingCertificateUseCase: ShareTrainingCertificateUseCase,
    private val trainingService: TrainingService
): ViewModel() {
    @AssistedFactory
    interface TrainingDetailViewModelFactory {
        fun create(trainingId: Int): TrainingDetailViewModel
    }

    var state by mutableStateOf(TrainingDetailState())
        private set

    var lessons = mutableListOf<FetchLessonResponse>()
        private set

    init {
        fetchInformation()
    }

    fun downloadCertificate() {
        viewModelScope.launch {
            try {
                state = state.copy(
                    isDownloadingCertificate = true,
                    status = AppStatusCodeEnum.DOWNLOADING_TRAINING_CERTIFICATE
                )

                shareTrainingCertificateUseCase(App.context, trainingId)
                state = state.copy(isDownloadingCertificate = false)
            } catch (e: HttpConsumerException) {
                state = state.copy(isDownloadingCertificate = true)
            }
        }
    }

    fun fetchInformation() {
        viewModelScope.launch {
            try {
                state = state.copy(isFetchingTrainingInformation = true)

                lessons.addAll(lessonService.all(trainingId))

                state = state.copy(
                    hasFetchTrainingInformationFailed = false,
                    training = trainingService.find(trainingId)
                )
            } catch (e: Exception) {
                state = state.copy(hasFetchTrainingInformationFailed = true)
            } finally {
                state = state.copy(isFetchingTrainingInformation = false)
            }
        }
    }

    fun stopProcessing() {
        state = state.copy(isDownloadingCertificate = false)
    }

    fun updateProgress() {
        if (state.training == null) return

        val completedLessonIds = App.preferences.getIntSet(AppPreferences.COMPLETED_LESSON_IDS, setOf())

        completedLessonIds.forEach { lessonId ->
            val lesson = lessons.find { it.id == lessonId }

            if (lesson?.isCompleted == false) {
                lesson.isCompleted = true

                val completedLessonsCount = state.training!!.completedLessonsCount + 1
                val percentage = completedLessonsCount * 100f / state.training!!.lessonsCount

                state = state.copy(training = state.training?.copy(completedLessonsCount = completedLessonsCount, progressPercentage = percentage))
            }
        }

        val edit = App.preferences.getPreferences().edit()
        edit.remove(AppPreferences.COMPLETED_LESSON_IDS)

        if (state.training!!.progressPercentage == 100f) edit.putInt(AppPreferences.COMPLETED_TRAINING, state.training!!.id)
        else if (state.training!!.progressPercentage != null && state.training!!.progressPercentage!! > 0f) {
            edit.putInt(AppPreferences.IN_PROGRESS_TRAINING, state.training!!.id)
        }

        edit.apply()
    }
}