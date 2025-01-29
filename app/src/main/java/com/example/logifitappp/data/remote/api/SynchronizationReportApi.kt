package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.requests.SynchronizationReportRequest
import com.example.logifitappp.data.remote.dto.response.SynchronizationReportResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SynchronizationReportApi {
    @POST("api/reports/detail")
    suspend fun fetchReport(@Body sleepReportRequest: SynchronizationReportRequest): Response<SynchronizationReportResponse>
}

