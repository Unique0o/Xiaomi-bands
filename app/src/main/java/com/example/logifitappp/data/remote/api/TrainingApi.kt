package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.response.FetchTrainingResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TrainingApi {
    @GET("api/training_list")
    suspend fun all(): Response<List<FetchTrainingResponse>>

    @GET("api/training_certificate/{id}")
    suspend fun download(@Path("id") trainingId: Int): Response<ResponseBody>

    @GET("api/trainings/{id}")
    suspend fun find(@Path("id") trainingId: Int): Response<FetchTrainingResponse>
}