package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.requests.StoreAudioRequest
import com.example.logifitappp.data.remote.dto.response.StoreAudioResponse
import retrofit2.Response

interface AudioRepository {
    suspend fun store(userId: Int, storeAudioRequest: StoreAudioRequest): Response<StoreAudioResponse>
}