package com.example.logifitappp.domain.service

import com.example.logifitappp.domain.repository.TrainingRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrainingService @Inject constructor(
    private val trainingRepository: TrainingRepository
) {
    suspend fun all() = withContext(Dispatchers.IO) {
        try {
            val response = trainingRepository.all()

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

            return@withContext response.body()
                ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun download(trainingId: Int) = withContext(Dispatchers.IO) {
        try {
            val response = trainingRepository.download(trainingId)

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun find(trainingId: Int) = withContext(Dispatchers.IO) {
        try {
            val response = trainingRepository.find(trainingId)

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }
}