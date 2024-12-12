package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.requests.AssociateWearableRequest
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import com.example.logifitappp.data.remote.dto.response.WearableAuthenticationKeyResponse
import retrofit2.Response

interface WearableRepository {
    suspend fun associate(userId: Int, associateWearableRequest: AssociateWearableRequest): Response<GeneralResponse>

    suspend fun fetchAuthenticationKey(mac: String): Response<WearableAuthenticationKeyResponse>
}