package com.example.logifitappp.domain.service

import com.example.logifitappp.data.remote.dto.requests.StoreOccupationalInformationRequest
import com.example.logifitappp.data.remote.dto.requests.StorePersonalInformationRequest
import com.example.logifitappp.data.remote.dto.requests.StoreRosterRequest
import com.example.logifitappp.data.remote.dto.response.GeneralErrorResponse
import com.example.logifitappp.domain.repository.UserRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class UserService @Inject constructor(private val userRepository: UserRepository) {
    suspend fun fetch() = withContext(Dispatchers.IO) {
        try {
            val response = userRepository.fetch()

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))

            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: HttpException) {
            throw HttpConsumerException(AppStatusCodeEnum.fromCode(e.code()))
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun fetchViewDetails(userIdentifier: Int, page: Int) = withContext(Dispatchers.IO) {
        try {
            val response = userRepository.fetchViewDetails(userIdentifier, page)

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun storeOccupationalInformation(userId: Int, request: StoreOccupationalInformationRequest) = withContext(Dispatchers.IO) {
        try {
            val response = userRepository.storeOccupationalInformation(userId, request)

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))

            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: HttpException) {
            throw HttpConsumerException(AppStatusCodeEnum.fromCode(e.code()))
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun storePersonalInformation(userId: Int, request: StorePersonalInformationRequest) = withContext(Dispatchers.IO) {
        try {
            val response = userRepository.storePersonalInformation(userId, request)

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))

            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: HttpException) {
            throw HttpConsumerException(AppStatusCodeEnum.fromCode(e.code()))
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun storeRosterInformation(request: StoreRosterRequest) = withContext(Dispatchers.IO) {
        try {
            val response = userRepository.storeRosterInformation(request)

            if (!response.isSuccessful) {
                response.errorBody()?.let {
                    val errorResponse =
                        Gson().fromJson(it.string(), GeneralErrorResponse::class.java)

                    if (errorResponse.code == 403) throw HttpConsumerException(AppStatusCodeEnum.FAILED_ROSTER_INFORMATION_STORAGE)

                    throw HttpConsumerException(AppStatusCodeEnum.fromCode(errorResponse.code))
                }

                throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
            }

            return@withContext response.body()
                ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: HttpConsumerException) {
            throw e
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }
}
