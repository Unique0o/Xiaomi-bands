package com.example.logifitappp.di.repositories

import com.example.logifitappp.di.services.requests.LoginRequest
import com.example.logifitappp.di.services.responses.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRepository {
    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}