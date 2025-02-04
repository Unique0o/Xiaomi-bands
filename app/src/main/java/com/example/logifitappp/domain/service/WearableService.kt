package com.example.logifitappp.domain.service

import com.example.logifitappp.data.remote.dto.requests.AssociateWearableRequest
import com.example.logifitappp.domain.repository.WearableRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class WearableService @Inject constructor(private val wearableRepository: WearableRepository) {
    suspend fun associate(userId: Int, associateWearableRequest: AssociateWearableRequest) = withContext(Dispatchers.IO) {
        try {
            val response = wearableRepository.associate(userId, associateWearableRequest)

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: HttpConsumerException) {
            throw e
        } catch (e: HttpException) {
            throw HttpConsumerException(AppStatusCodeEnum.fromCode(e.code()))
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun fetchAuthenticationKey(mac: String): String? = withContext(Dispatchers.IO) {
        try {
            val response = wearableRepository.fetchAuthenticationKey(mac)

            if (!response.isSuccessful) {
                println(response.code())
                throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))
            }

            val body = response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

            return@withContext body.key
        } catch (e: HttpException) {
            throw HttpConsumerException(AppStatusCodeEnum.fromCode(e.code()))
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun fetchXiaomiCredentials() = withContext(Dispatchers.IO) {
        try {
            val response = wearableRepository.fetchXiaomiCredentials()

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }
}