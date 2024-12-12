package com.example.logifitappp.viewmodel.states

import com.example.logifitappp.data.remote.dto.response.FetchLessonResponse

data class LessonDetailState(
    val canGoToNext: Boolean = true,
    val canGoToPrev: Boolean = true,
    val currentLessonId: Int = 0,
    val hasFetchLessonInformationFailed: Boolean = false,
    val hasMarkAsCompletedFailed: Boolean = false,
    val isFetchingLessonInformation: Boolean = true,
    val isMarkingAsCompleted: Boolean = false,
    var lesson: FetchLessonResponse? = null
)
