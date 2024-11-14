package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.response.StoreOccupationalInformationResponse
import com.example.logifitappp.data.remote.dto.response.StorePersonalInformationResponse
import com.example.logifitappp.data.remote.dto.requests.StoreOccupationalInformationRequest
import com.example.logifitappp.data.remote.dto.requests.StorePersonalInformationRequest
import com.example.logifitappp.data.remote.dto.response.FetchUserInformationResponse
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @POST("api/auth/me")
    suspend fun fetch(): Response<FetchUserInformationResponse>

    @PUT("api/user/update_data_laboral/{id}")
    suspend fun storeOccupationalInformation(
        @Path("id") userIdentifier: Int,
        @Body storeOccupationalInformationRequest: StoreOccupationalInformationRequest
    ): Response<StoreOccupationalInformationResponse>

    @PUT("api/user/update_data_personal/{id}")
    suspend fun storePersonalInformation(
        @Path("id") userIdentifier: Int,
        @Body storePersonalInformationRequest: StorePersonalInformationRequest
    ): Response<StorePersonalInformationResponse>
}