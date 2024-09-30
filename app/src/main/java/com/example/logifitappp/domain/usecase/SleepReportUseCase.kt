package com.example.logifitappp.domain.usecase

import com.example.logifitappp.data.remote.dto.requests.SleepReportRequest
import com.example.logifitappp.data.remote.dto.response.SleepReportResponse
import com.example.logifitappp.domain.service.SleepReportService
import javax.inject.Inject

class SleepReportUseCase @Inject constructor(private val sleepReportService: SleepReportService) {

    suspend operator fun invoke(date: String, tenantId: String): SleepReportResponse {
        return sleepReportService.fetchReport(SleepReportRequest(date, tenantId))
    }
}