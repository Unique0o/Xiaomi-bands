package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.requests.StorePersonalInformationRequest
import com.example.logifitappp.data.remote.dto.response.FetchUserInformationResponse
import com.example.logifitappp.data.remote.dto.response.StorePersonalInformationResponse
import retrofit2.Response

interface UserRepository {
    suspend fun fetch(): Response<FetchUserInformationResponse>

    suspend fun storePersonalInformation(userId: Int, request: StorePersonalInformationRequest): Response<StorePersonalInformationResponse>
}