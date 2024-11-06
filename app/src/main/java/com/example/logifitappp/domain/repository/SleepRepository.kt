package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.requests.StoreFatigueRequest
import com.example.logifitappp.data.remote.dto.requests.StoreHeartRateRequest
import com.example.logifitappp.data.remote.dto.requests.StoreSleepGraphicRequest
import com.example.logifitappp.data.remote.dto.requests.StoreSleepRequest
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import com.example.logifitappp.data.remote.dto.response.StoreSleepResponse
import retrofit2.Response

interface SleepRepository {
    suspend fun store(storeSleepRequest: StoreSleepRequest): Response<StoreSleepResponse>

    suspend fun storeFatigue(storeFatigueRequest: StoreFatigueRequest): Response<GeneralResponse>

    suspend fun storeGraphics(storeSleepGraphicRequest: StoreSleepGraphicRequest): Response<GeneralResponse>

    suspend fun storeHeartRates(storeHeartRateRequest: StoreHeartRateRequest): Response<GeneralResponse>
}