package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.response.RestParametersResponse
import com.example.logifitappp.data.remote.dto.response.TenantResponse
import retrofit2.Response
import retrofit2.http.GET

interface TenantApi {
    @GET("api/users/me/tenant")
    suspend fun fetch(): Response<TenantResponse>

    @GET("api/users/me/parameters")
    suspend fun fetchRestParameters(): Response<RestParametersResponse>
}