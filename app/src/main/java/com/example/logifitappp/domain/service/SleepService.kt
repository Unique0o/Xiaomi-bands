package com.example.logifitappp.domain.service

import com.example.logifitappp.data.remote.dto.requests.StoreFatigueRequest
import com.example.logifitappp.data.remote.dto.requests.StoreHeartRateRequest
import com.example.logifitappp.data.remote.dto.requests.StoreSleepGraphicRequest
import com.example.logifitappp.data.remote.dto.requests.StoreSleepRequest
import com.example.logifitappp.data.remote.dto.response.GeneralErrorResponse
import com.example.logifitappp.domain.repository.SleepRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class SleepService @Inject constructor(
    private val sleepRepository: SleepRepository
) {
    suspend fun store(storeSleepRequest: StoreSleepRequest) = withContext(Dispatchers.IO) {
        try {
            val response = sleepRepository.store(storeSleepRequest)

            if (!response.isSuccessful) {
                response.errorBody()?.let {
                    val errorResponse = Gson().fromJson(it.string(), GeneralErrorResponse::class.java)
                    throw HttpConsumerException(AppStatusCodeEnum.fromCode(errorResponse.code))
                }
            }

            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: HttpConsumerException) {
            throw e
        } catch (e: HttpException) {
            throw HttpConsumerException(AppStatusCodeEnum.fromCode(e.code()))
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun storeFatigue(storeFatigueRequest: StoreFatigueRequest) = withContext(Dispatchers.IO) {
        try {
            val response = sleepRepository.storeFatigue(storeFatigueRequest)

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

    suspend fun storeGraphics(storeSleepGraphicRequest: StoreSleepGraphicRequest) = withContext(Dispatchers.IO) {
        try {
            val response = sleepRepository.storeGraphics(storeSleepGraphicRequest)

            if (!response.isSuccessful) {
                response.errorBody()?.let {
                    val errorResponse = Gson().fromJson(it.string(), GeneralErrorResponse::class.java)
                    throw HttpConsumerException(AppStatusCodeEnum.fromCode(errorResponse.code))
                }
            }

            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: HttpConsumerException) {
            throw e
        } catch (e: HttpException) {
            throw HttpConsumerException(AppStatusCodeEnum.fromCode(e.code()))
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun storeHeartRates(storeHeartRateRequest: StoreHeartRateRequest) = withContext(Dispatchers.IO) {
        try {
            val response = sleepRepository.storeHeartRates(storeHeartRateRequest)

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
}