package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.requests.StoreAudioRequest
import com.example.logifitappp.data.remote.dto.response.StoreAudioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AudioApi {
    @POST("api/store_action_audio/{id}")
    suspend fun store(@Path("id") userId: Int, @Body storeAudioRequest: StoreAudioRequest): Response<StoreAudioResponse>
}