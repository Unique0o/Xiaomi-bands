package com.example.logifitappp.domain.service

import com.example.logifitappp.data.remote.dto.requests.StoreAudioRequest
import com.example.logifitappp.domain.repository.AudioRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AudioService @Inject constructor(private val audioRepository: AudioRepository) {
    suspend fun store(userId: Int, storeAudioRequest: StoreAudioRequest) = withContext(Dispatchers.IO) {
        try {
            val response = audioRepository.store(userId, storeAudioRequest)

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }
}