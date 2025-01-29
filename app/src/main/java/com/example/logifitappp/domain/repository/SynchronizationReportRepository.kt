package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.requests.SynchronizationReportRequest
import com.example.logifitappp.data.remote.dto.response.SynchronizationReportResponse
import okhttp3.ResponseBody
import retrofit2.Response

interface SynchronizationReportRepository {
    suspend fun download(reportId: Int, tenantId: Int, shiftId: Int?, groupId: Int?): Response<ResponseBody>

    suspend fun fetchReport(synchronizationReportRequest: SynchronizationReportRequest): Response<SynchronizationReportResponse>
}