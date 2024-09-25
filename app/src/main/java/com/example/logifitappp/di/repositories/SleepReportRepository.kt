package com.example.logifitappp.di.repositories

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.File
import com.example.logifitappp.di.services.requests.SleepReportRequest
import com.example.logifitappp.di.services.responses.SleepReportResponse
import retrofit2.Response

interface SleepReportRepository {
    @GET("api/report/export_format")
    suspend fun fetchWeeklyReport(
       // @Query("report_id") reportId: Int,
      //  @Query("tenant_id") tenantId: String,
        @Query("shift_id") shiftId: Int?,
        @Query("group_id") groupId: Int?
    ): File

    @POST("api/reports/detail")
    suspend fun fetchReport(@Body sleepReportRequest: SleepReportRequest): Response<SleepReportResponse>
}

