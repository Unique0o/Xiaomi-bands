package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.core.App
import com.example.logifitappp.core.AppPreferences
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
    @Assisted private val lessonIds: Array<Int>,
    @Assisted private val user: UserModel?,
    private val lessonService: LessonService
): ViewModel() {
    @AssistedFactory
    interface LessonDetailViewModelFactory {
        fun create(lessonId: Int, lessonIds: Array<Int>, user: UserModel?): LessonDetailViewModel
    }

    var state by mutableStateOf(LessonDetailState())
        private set

    init {
        val currentIndex = lessonIds.indexOf(lessonId)

        state = state.copy(
            canGoToNext = currentIndex != lessonIds.lastIndex,
            canGoToPrev = currentIndex > 0,
            currentLessonId = lessonId
        )

        fetchInformation()
    }

    fun fetchInformation() {
        viewModelScope.launch {
            try {
                state = state.copy(isFetchingLessonInformation = true)

                state = state.copy(
                    hasFetchLessonInformationFailed = false,
                    lesson = lessonService.find(state.currentLessonId)
                )
            } catch (e: Exception) {
                state = state.copy(hasFetchLessonInformationFailed = true)
            } finally {
                state = state.copy(isFetchingLessonInformation = false)
            }
        }
    }

    fun markAsCompleted() {
        if (user == null) return

        if (state.lesson == null) return

        viewModelScope.launch {
            try {
                state = state.copy(isMarkingAsCompleted = true)

                val response = lessonService.markAsCompleted(MarkAsCompletedLessonRequest(
                    lesson_id = state.lesson!!.id,
                    user_id = user.id
                ))

                if (response) {
                    state = state.copy(lesson = state.lesson?.copy(isCompleted = true))
                    App.preferences.addIntToSet(AppPreferences.COMPLETED_LESSON_IDS, state.lesson!!.id)
                }

                state = state.copy(hasMarkAsCompletedFailed = false)
            } catch (e: Exception) {
                state = state.copy(hasMarkAsCompletedFailed = true)
            } finally {
                state = state.copy(isMarkingAsCompleted = false)
            }
        }
    }

    fun nextLesson() {
        val currentIndex = lessonIds.indexOf(state.currentLessonId)

        if (currentIndex == lessonIds.lastIndex) return

        val nextLessonId = lessonIds[currentIndex + 1]

        state = state.copy(
            canGoToNext = currentIndex + 1 != lessonIds.lastIndex,
            canGoToPrev = true,
            currentLessonId = nextLessonId
        )

        fetchInformation()
    }

    fun prevLesson() {
        val currentIndex = lessonIds.indexOf(state.currentLessonId)

        if (currentIndex == 0) return

        val prevLessonId = lessonIds[currentIndex - 1]

        state = state.copy(
            canGoToNext = true,
            canGoToPrev = currentIndex - 1 > 0,
            currentLessonId = prevLessonId
        )

        fetchInformation()
    }
}