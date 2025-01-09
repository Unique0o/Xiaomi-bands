package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.api.UserApi
import com.example.logifitappp.data.remote.dto.requests.StoreOccupationalInformationRequest
import com.example.logifitappp.data.remote.dto.requests.StorePersonalInformationRequest
import com.example.logifitappp.data.remote.dto.requests.StoreRosterRequest
import com.example.logifitappp.data.remote.dto.response.FetchUserInformationResponse
import com.example.logifitappp.data.remote.dto.response.FetchUserViewDetailsResponse
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import com.example.logifitappp.data.remote.dto.response.StoreOccupationalInformationResponse
import com.example.logifitappp.data.remote.dto.response.StorePersonalInformationResponse
import com.example.logifitappp.domain.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
): UserRepository {
    override suspend fun fetch(): Response<FetchUserInformationResponse> {
        return userApi.fetch()
    }

    override suspend fun fetchViewDetails(userIdentifier: Int, page: Int): Response<FetchUserViewDetailsResponse> {
        return userApi.fetchViewDetails(userIdentifier, page)
    }

    override suspend fun storeOccupationalInformation(userId: Int, request: StoreOccupationalInformationRequest): Response<StoreOccupationalInformationResponse> {
        return userApi.storeOccupationalInformation(userId, request)
    }

    override suspend fun storePersonalInformation(userId: Int, request: StorePersonalInformationRequest): Response<StorePersonalInformationResponse> {
        return userApi.storePersonalInformation(userId, request)
    }

    override suspend fun storeRosterInformation(request: StoreRosterRequest): Response<GeneralResponse> {
        return userApi.storeRosterInformation(request)
    }

    override suspend fun updateProfilePhoto(userId: Int, photo: MultipartBody.Part): Response<ResponseBody> {
        return userApi.updateProfilePhoto(userId, photo)
    }
}