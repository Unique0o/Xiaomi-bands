package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.api.WearableApi
import com.example.logifitappp.data.remote.dto.requests.AssociateWearableRequest
import com.example.logifitappp.data.remote.dto.response.FetchXiaomiCredentialResponse
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import com.example.logifitappp.data.remote.dto.response.WearableAuthenticationKeyResponse
import com.example.logifitappp.domain.repository.WearableRepository
import retrofit2.Response
import javax.inject.Inject

class WearableRepositoryImpl @Inject constructor(
    private val wearableApi: WearableApi
): WearableRepository {
    override suspend fun associate(userId: Int, associateWearableRequest: AssociateWearableRequest): Response<GeneralResponse> {
        return wearableApi.associate(userId, associateWearableRequest)
    }

    override suspend fun fetchAuthenticationKey(mac: String): Response<WearableAuthenticationKeyResponse> {
        return wearableApi.fetchAuthenticationKey(mac)
    }

    override suspend fun fetchXiaomiCredentials(): Response<List<FetchXiaomiCredentialResponse>> {
        return wearableApi.fetchXiaomiCredentials()
    }
}