package com.example.logifitappp.di.services

import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.di.repositories.AuthRepository
import com.example.logifitappp.di.services.requests.LoginRequest
import com.example.logifitappp.di.services.responses.toUser
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class AuthService @Inject constructor(private val authRepository: AuthRepository) {
    suspend fun login(loginRequest: LoginRequest): UserModel = withContext(Dispatchers.IO) {
        try {
            val response = authRepository.login(loginRequest)

            if (!response.isSuccessful) {
                println(response.code())
                throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))
            }

            val body = response.body() ?:  throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

            return@withContext body.toUser()
        } catch (e: HttpException) {
            throw HttpConsumerException(AppStatusCodeEnum.fromCode(e.code()))
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }
}