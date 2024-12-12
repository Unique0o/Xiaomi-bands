package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.api.LessonApi
import com.example.logifitappp.data.remote.dto.requests.MarkAsCompletedLessonRequest
import com.example.logifitappp.data.remote.dto.response.FetchLessonResponse
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import com.example.logifitappp.domain.repository.LessonRepository
import retrofit2.Response
import javax.inject.Inject

class LessonRepositoryImpl @Inject constructor(
    private val lessonApi: LessonApi
): LessonRepository {
    override suspend fun all(trainingId: Int): Response<List<FetchLessonResponse>> {
        return lessonApi.all(trainingId)
    }

    override suspend fun find(lessonId: Int): Response<FetchLessonResponse> {
        return lessonApi.find(lessonId)
    }

    override suspend fun markAsCompleted(markAsCompletedLessonRequest: MarkAsCompletedLessonRequest): Response<GeneralResponse> {
        return lessonApi.markAsCompleted(markAsCompletedLessonRequest)
    }
}