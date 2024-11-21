package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.requests.SleepWrittenDataRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SleepWrittenDataApi {
    @POST("api/store_sleeps_app")
    suspend fun saveSleepRecord(
        @Body request: SleepWrittenDataRequest
    ): Result<Unit>
}
