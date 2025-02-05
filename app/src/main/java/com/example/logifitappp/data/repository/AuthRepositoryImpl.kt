package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.api.AuthApi
import com.example.logifitappp.data.remote.dto.requests.FirebaseKeyRequest
import com.example.logifitappp.data.remote.dto.requests.LoginRequest
import com.example.logifitappp.data.remote.dto.requests.PasswordRecoveryRequest
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import com.example.logifitappp.data.remote.dto.response.LoginResponse
import com.example.logifitappp.domain.repository.AuthRepository
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
): AuthRepository {

    override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return authApi.login(loginRequest)
    }

    override suspend fun recoverPassword(passwordRecoveryRequest: PasswordRecoveryRequest): Response<GeneralResponse> {
        return authApi.recoverPassword(passwordRecoveryRequest)
    }

    override suspend fun updateNotificationToken(userId: Int, firebaseKeyRequest: FirebaseKeyRequest): Response<GeneralResponse> {
        return authApi.updateNotificationToken(userId, firebaseKeyRequest)
    }
}