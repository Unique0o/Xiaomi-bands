package com.example.logifitappp.domain.service


import com.example.logifitappp.data.remote.dto.requests.StorePersonalInformationRequest
import com.example.logifitappp.domain.repository.UserRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import javax.inject.Inject

class UserService @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun storePersonalInformation(
        userId: Int,
        request: StorePersonalInformationRequest
    ) {
        try {
            val response = userRepository.store(userId, request)

            if (!response.isSuccessful) {
                throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))
            }

            response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

        } catch (e: Exception) {
            when (e) {
                is HttpConsumerException -> throw e
                else -> throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
            }
        }
    }
}
