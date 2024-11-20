package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import com.example.logifitappp.data.remote.dto.response.WearableAuthenticationKeyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WearableApi {
    @POST("api/workers/{id}/associate-device")
    suspend fun associate(@Path("id") userId: Int): Response<GeneralResponse>

    @GET("api/devices/by/mac/{mac}")
    suspend fun fetchAuthenticationKey(@Path("mac") mac: String): Response<WearableAuthenticationKeyResponse>
}