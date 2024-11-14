package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.requests.StoreFatigueRequest
import com.example.logifitappp.data.remote.dto.requests.StoreHeartRateRequest
import com.example.logifitappp.data.remote.dto.requests.StoreSleepGraphicRequest
import com.example.logifitappp.data.remote.dto.requests.StoreSleepRequest
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import com.example.logifitappp.data.remote.dto.response.StoreSleepResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SleepApi {
    @POST("api/sleeps")
    fun store(@Body storeSleepRequest: StoreSleepRequest): Response<StoreSleepResponse>

    @POST("api/fatigues")
    fun storeFatigue(@Body storeFatigueRequest: StoreFatigueRequest): Response<GeneralResponse>

    @POST("api/sleeps_graphic")
    fun storeGraphics(@Body storeSleepGraphicRequest: StoreSleepGraphicRequest): Response<GeneralResponse>

    @POST("api/heart_rates")
    fun storeHeartRates(@Body storeHeartRateRequest: StoreHeartRateRequest): Response<GeneralResponse>
}