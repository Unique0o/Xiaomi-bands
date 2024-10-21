package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.response.RestParametersResponse
import com.example.logifitappp.data.remote.dto.response.TenantResponse
import retrofit2.Response

interface TenantRepository {
    suspend fun fetch(): Response<TenantResponse>

    suspend fun fetchRestParameters(): Response<RestParametersResponse>
}