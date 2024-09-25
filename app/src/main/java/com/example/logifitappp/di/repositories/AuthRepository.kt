package com.example.logifitappp.di.repositories

import com.example.logifitappp.di.services.requests.FirebaseKeyRequest
import com.example.logifitappp.di.services.requests.LoginRequest
import com.example.logifitappp.di.services.requests.PasswordRecoveryRequest
import com.example.logifitappp.di.services.responses.GeneralResponse
import com.example.logifitappp.di.services.responses.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthRepository {
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