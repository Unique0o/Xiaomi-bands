package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.requests.AssociateWearableRequest
import com.example.logifitappp.data.remote.dto.requests.StoreXiaomiMacRequest
import com.example.logifitappp.data.remote.dto.response.FetchXiaomiCredentialResponse
import com.example.logifitappp.data.remote.dto.response.FetchXiaomiMacResponse
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import com.example.logifitappp.data.remote.dto.response.StoreXiaomiMacResponse
import com.example.logifitappp.data.remote.dto.response.WearableAuthenticationKeyResponse
import retrofit2.Response

interface WearableRepository {
    suspend fun associate(userId: Int, associateWearableRequest: AssociateWearableRequest): Response<GeneralResponse>

    suspend fun fetchAuthenticationKey(mac: String): Response<WearableAuthenticationKeyResponse>

    suspend fun fetchXiaomiCredentials(): Response<List<FetchXiaomiCredentialResponse>>

    suspend fun fetchXiaomiMacs(credentialId: Int, password: String, type: String?, username: String): Response<List<FetchXiaomiMacResponse>>

    suspend fun storeXiaomiMacs(storeXiaomiMacRequest: StoreXiaomiMacRequest): Response<StoreXiaomiMacResponse>
}