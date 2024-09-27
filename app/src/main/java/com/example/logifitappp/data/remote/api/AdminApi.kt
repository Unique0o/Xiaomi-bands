package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.response.WorkerResponse
import retrofit2.http.GET
import retrofit2.Response

interface AdminApi {
    @GET("api/users/me/workers")
    suspend fun fetchWorkers(): Response<WorkerResponse>
}