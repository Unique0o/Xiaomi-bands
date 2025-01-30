package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.dto.requests.SynchronizationReportRequest
import com.example.logifitappp.data.remote.dto.response.SynchronizationReportResponse
import javax.inject.Inject
import retrofit2.Response
import com.example.logifitappp.data.remote.api.SynchronizationReportApi
import com.example.logifitappp.domain.repository.SynchronizationReportRepository
import okhttp3.ResponseBody

class SynchronizationReportRepositoryImpl @Inject constructor(
    private val sleepReportApi: SynchronizationReportApi
): SynchronizationReportRepository {
    override suspend fun download(reportId: Int, tenantId: Int, shiftId: Int?, groupId: Int?): Response<ResponseBody> {
        return sleepReportApi.download(reportId, tenantId, shiftId, groupId)
    }

    override suspend fun fetchReport(synchronizationReportRequest: SynchronizationReportRequest): Response<SynchronizationReportResponse> {
        return sleepReportApi.fetchReport(synchronizationReportRequest)
    }
}
