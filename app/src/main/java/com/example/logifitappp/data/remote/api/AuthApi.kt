package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.requests.LoginRequest
import com.example.logifitappp.data.remote.dto.requests.PasswordRecoveryRequest
import com.example.logifitappp.data.remote.dto.requests.FirebaseKeyRequest
import com.example.logifitappp.data.remote.dto.response.UserResponse
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import com.example.logifitappp.data.remote.dto.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthApi {

    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/auth/reset")
    suspend fun recoverPassword(@Body passwordRecoveryRequest: PasswordRecoveryRequest): Response<GeneralResponse>

    @PUT("api/users/{id}/associate-fcm-key")
    suspend fun updateNotificationToken(
        @Path("id") userIdentifier: Int,
        @Body firebaseKeyRequest: FirebaseKeyRequest
    ): Response<GeneralResponse>
}