package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.response.StoreOccupationalInformationResponse
import com.example.logifitappp.data.remote.dto.response.StorePersonalInformationResponse
import com.example.logifitappp.data.remote.dto.requests.StoreOccupationalInformationRequest
import com.example.logifitappp.data.remote.dto.requests.StorePersonalInformationRequest
import com.example.logifitappp.data.remote.dto.requests.StoreRosterRequest
import com.example.logifitappp.data.remote.dto.response.FetchUserInformationResponse
import com.example.logifitappp.data.remote.dto.response.FetchUserViewDetailsResponse
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @POST("api/auth/me")
    suspend fun fetch(): Response<FetchUserInformationResponse>

    @GET("api/view_user/{id}")
    suspend fun fetchViewDetails(@Path("id") userIdentifier: Int, @Query("page") page: Int): Response<FetchUserViewDetailsResponse>

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

    @POST("api/users/roster")
    suspend fun storeRosterInformation(@Body storeRosterRequest: StoreRosterRequest): Response<GeneralResponse>

    @Multipart
    @POST("api/update_profile/{id}")
    suspend fun updateProfilePhoto(@Path("id") userId: Int, @Part photo: MultipartBody.Part): Response<ResponseBody>
}