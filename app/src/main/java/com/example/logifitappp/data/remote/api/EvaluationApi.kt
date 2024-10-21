package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.response.EvaluationResultResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EvaluationApi {
    @GET("api/result_user/{id}")
    suspend fun fetchResults(@Path("id") userId: Int): Response<List<EvaluationResultResponse>>
}