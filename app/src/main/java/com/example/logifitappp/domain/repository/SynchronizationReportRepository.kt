package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.requests.SynchronizationReportRequest
import com.example.logifitappp.data.remote.dto.response.SynchronizationReportResponse
import retrofit2.Response

interface SynchronizationReportRepository {
    suspend fun fetchReport(synchronizationReportRequest: SynchronizationReportRequest): Response<SynchronizationReportResponse>
}