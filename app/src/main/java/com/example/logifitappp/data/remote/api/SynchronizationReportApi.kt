package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.requests.SynchronizationReportRequest
import com.example.logifitappp.data.remote.dto.response.SynchronizationReportResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SynchronizationReportApi {
    @GET("api/report/export_format")
    suspend fun download(@Query("report_id") reportId: Int, @Query("tenant_id") tenantId: Int, @Query("shift_id") shiftId: Int?, @Query("group_id") groupId: Int?): Response<ResponseBody>

    @POST("api/reports/detail")
    suspend fun fetchReport(@Body sleepReportRequest: SynchronizationReportRequest): Response<SynchronizationReportResponse>
}

