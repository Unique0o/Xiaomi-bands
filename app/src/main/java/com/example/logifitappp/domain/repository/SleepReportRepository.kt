package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.requests.SleepReportRequest
import com.example.logifitappp.data.remote.dto.response.SleepReportResponse
import retrofit2.Response

interface SleepReportRepository {
    suspend fun fetchReport(sleepReportRequest: SleepReportRequest): Response<SleepReportResponse>
}