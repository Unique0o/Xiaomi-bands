package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.requests.FirebaseKeyRequest
import com.example.logifitappp.data.remote.dto.requests.LoginRequest
import com.example.logifitappp.data.remote.dto.requests.PasswordRecoveryRequest
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import com.example.logifitappp.data.remote.dto.response.LoginResponse
import retrofit2.Response

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse>

    suspend fun recoverPassword(passwordRecoveryRequest: PasswordRecoveryRequest): Response<GeneralResponse>

    suspend fun updateNotificationToken(userId: Int, firebaseKeyRequest: FirebaseKeyRequest): Response<GeneralResponse>
}