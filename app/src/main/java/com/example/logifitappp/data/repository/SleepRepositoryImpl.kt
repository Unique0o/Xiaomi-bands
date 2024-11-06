package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.api.SleepApi
import com.example.logifitappp.data.remote.dto.requests.StoreFatigueRequest
import com.example.logifitappp.data.remote.dto.requests.StoreHeartRateRequest
import com.example.logifitappp.data.remote.dto.requests.StoreSleepGraphicRequest
import com.example.logifitappp.data.remote.dto.requests.StoreSleepRequest
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import com.example.logifitappp.data.remote.dto.response.StoreSleepResponse
import com.example.logifitappp.domain.repository.SleepRepository
import retrofit2.Response
import javax.inject.Inject

class SleepRepositoryImpl @Inject constructor(
    private val sleepApi: SleepApi
): SleepRepository {
    override suspend fun store(storeSleepRequest: StoreSleepRequest): Response<StoreSleepResponse> {
        return sleepApi.store(storeSleepRequest)
    }

    override suspend fun storeFatigue(storeFatigueRequest: StoreFatigueRequest): Response<GeneralResponse> {
        return sleepApi.storeFatigue(storeFatigueRequest)
    }

    override suspend fun storeGraphics(storeSleepGraphicRequest: StoreSleepGraphicRequest): Response<GeneralResponse> {
        return sleepApi.storeGraphics(storeSleepGraphicRequest)
    }

    override suspend fun storeHeartRates(storeHeartRateRequest: StoreHeartRateRequest): Response<GeneralResponse> {
        return sleepApi.storeHeartRates(storeHeartRateRequest)
    }
}