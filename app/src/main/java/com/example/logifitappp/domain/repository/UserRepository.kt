package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.requests.StoreOccupationalInformationRequest
import com.example.logifitappp.data.remote.dto.requests.StorePersonalInformationRequest
import com.example.logifitappp.data.remote.dto.requests.StoreRosterRequest
import com.example.logifitappp.data.remote.dto.response.FetchUserInformationResponse
import com.example.logifitappp.data.remote.dto.response.FetchUserViewDetailsResponse
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import com.example.logifitappp.data.remote.dto.response.StoreOccupationalInformationResponse
import com.example.logifitappp.data.remote.dto.response.StorePersonalInformationResponse
import retrofit2.Response

interface UserRepository {
    suspend fun fetch(): Response<FetchUserInformationResponse>

    suspend fun fetchViewDetails(userIdentifier: Int, page: Int): Response<FetchUserViewDetailsResponse>

    suspend fun storeOccupationalInformation(userId: Int, request: StoreOccupationalInformationRequest): Response<StoreOccupationalInformationResponse>

    suspend fun storePersonalInformation(userId: Int, request: StorePersonalInformationRequest): Response<StorePersonalInformationResponse>

    suspend fun storeRosterInformation(request: StoreRosterRequest): Response<GeneralResponse>
}