package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.requests.MarkAsCompletedLessonRequest
import com.example.logifitappp.data.remote.dto.response.FetchLessonResponse
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import retrofit2.Response

interface LessonRepository {
    suspend fun all(trainingId: Int): Response<List<FetchLessonResponse>>

    suspend fun find(lessonId: Int): Response<FetchLessonResponse>

    suspend fun markAsCompleted(markAsCompletedLessonRequest: MarkAsCompletedLessonRequest): Response<GeneralResponse>
}