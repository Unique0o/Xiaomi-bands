package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.data.remote.dto.requests.MarkAsCompletedLessonRequest
import com.example.logifitappp.domain.service.LessonService
import com.example.logifitappp.viewmodel.states.LessonDetailState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = LessonDetailViewModel.LessonDetailViewModelFactory::class)
class LessonDetailViewModel @AssistedInject constructor(
    @Assisted private val lessonId: Int,
    @Assisted private val user: UserModel,
    private val lessonService: LessonService
): ViewModel() {
    @AssistedFactory
    interface LessonDetailViewModelFactory {
        fun create(lessonId: Int, user: UserModel): LessonDetailViewModel
    }

    var state by mutableStateOf(LessonDetailState())
        private set

    init {
        fetchInformation()
    }

    fun fetchInformation() {
        viewModelScope.launch {
            try {
                state = state.copy(isFetchingLessonInformation = true)

                state = state.copy(
                    hasFetchLessonInformationFailed = false,
                    lesson = lessonService.find(lessonId)
                )
            } catch (e: Exception) {
                state = state.copy(hasFetchLessonInformationFailed = true)
            } finally {
                state = state.copy(isFetchingLessonInformation = false)
            }
        }
    }

    fun markAsCompleted() {
        viewModelScope.launch {
            try {
                state = state.copy(isMarkingAsCompleted = true)

                val response = lessonService.markAsCompleted(MarkAsCompletedLessonRequest(
                    lesson_id = lessonId,
                    user_id = user.id
                ))

                if (response) state = state.copy(lesson = state.lesson?.copy(isCompleted = true))

                state = state.copy(hasMarkAsCompletedFailed = false)
            } catch (e: Exception) {
                state = state.copy(hasMarkAsCompletedFailed = true)
            } finally {
                state = state.copy(isMarkingAsCompleted = false)
            }
        }
    }
}