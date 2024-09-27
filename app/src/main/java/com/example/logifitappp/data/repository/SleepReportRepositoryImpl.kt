package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.dto.requests.SleepReportRequest
import com.example.logifitappp.data.remote.dto.response.SleepReportResponse
import javax.inject.Inject
import retrofit2.Response
import com.example.logifitappp.data.remote.api.SleepReportApi
import com.example.logifitappp.domain.repository.SleepReportRepository

class SleepReportRepositoryImpl @Inject constructor(
    private val sleepReportApi: SleepReportApi)
    : SleepReportRepository {

    override suspend fun fetchReport(sleepReportRequest: SleepReportRequest): Response<SleepReportResponse> {
        return sleepReportApi.fetchReport(sleepReportRequest)
    }
}
