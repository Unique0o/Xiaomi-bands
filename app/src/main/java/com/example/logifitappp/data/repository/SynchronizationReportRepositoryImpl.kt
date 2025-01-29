package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.dto.requests.SynchronizationReportRequest
import com.example.logifitappp.data.remote.dto.response.SynchronizationReportResponse
import javax.inject.Inject
import retrofit2.Response
import com.example.logifitappp.data.remote.api.SynchronizationReportApi
import com.example.logifitappp.domain.repository.SynchronizationReportRepository

class SynchronizationReportRepositoryImpl @Inject constructor(
    private val sleepReportApi: SynchronizationReportApi
): SynchronizationReportRepository {
    override suspend fun fetchReport(synchronizationReportRequest: SynchronizationReportRequest): Response<SynchronizationReportResponse> {
        return sleepReportApi.fetchReport(synchronizationReportRequest)
    }
}
