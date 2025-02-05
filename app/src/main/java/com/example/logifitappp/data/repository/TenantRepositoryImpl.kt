package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.api.TenantApi
import com.example.logifitappp.data.remote.dto.response.RestParametersResponse
import com.example.logifitappp.data.remote.dto.response.TenantResponse
import com.example.logifitappp.domain.repository.TenantRepository
import retrofit2.Response
import javax.inject.Inject

class TenantRepositoryImpl @Inject constructor(
    private val tenantApi: TenantApi
): TenantRepository {
    override suspend fun fetch(): Response<TenantResponse> {
        return tenantApi.fetch()
    }

    override suspend fun fetchRestParameters(): Response<RestParametersResponse> {
        return tenantApi.fetchRestParameters()
    }
}