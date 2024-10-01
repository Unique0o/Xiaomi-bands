package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.response.WearableAuthenticationKeyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WearableRepository {
    @GET("api/devices/by/mac/{mac}")
    suspend fun fetchAuthenticationKey(@Path("mac") mac: String): Response<WearableAuthenticationKeyResponse>
}