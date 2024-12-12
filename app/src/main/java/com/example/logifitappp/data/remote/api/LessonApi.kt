package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.requests.MarkAsCompletedLessonRequest
import com.example.logifitappp.data.remote.dto.response.FetchLessonResponse
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LessonApi {
    @GET("api/lesson_list/{id}")
    suspend fun all(@Path("id") trainingId: Int): Response<List<FetchLessonResponse>>

    @GET("api/lessons/{id}")
    suspend fun find(@Path("id") lessonId: Int): Response<FetchLessonResponse>

    @POST("api/lesson_completed")
    suspend fun markAsCompleted(@Body markAsCompletedLessonRequest: MarkAsCompletedLessonRequest): Response<GeneralResponse>
}