package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.requests.SleepReportRequest
import com.example.logifitappp.data.remote.dto.response.SleepReportResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SleepReportApi {
    @POST("api/reports/detail")
    suspend fun fetchReport(@Body sleepReportRequest: SleepReportRequest): Response<SleepReportResponse>
}

