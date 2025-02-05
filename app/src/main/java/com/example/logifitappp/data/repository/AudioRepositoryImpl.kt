package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.api.AudioApi
import com.example.logifitappp.data.remote.dto.requests.StoreAudioRequest
import com.example.logifitappp.data.remote.dto.response.StoreAudioResponse
import com.example.logifitappp.domain.repository.AudioRepository
import retrofit2.Response
import javax.inject.Inject

class AudioRepositoryImpl @Inject constructor(private val audioApi: AudioApi): AudioRepository {
    override suspend fun store(userId: Int, storeAudioRequest: StoreAudioRequest): Response<StoreAudioResponse> {
        return audioApi.store(userId, storeAudioRequest)
    }
}