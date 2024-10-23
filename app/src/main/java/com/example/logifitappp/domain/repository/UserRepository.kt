package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.data.remote.dto.requests.StorePersonalInformationRequest
import com.example.logifitappp.data.remote.dto.response.StorePersonalInformationResponse
import retrofit2.Response

interface UserRepository {
    suspend fun isUserAdmin(): Boolean

    suspend fun createOrUpdate(user: UserModel)

    suspend fun store(userId: Int, request: StorePersonalInformationRequest): Response<StorePersonalInformationResponse>

    suspend fun getLoggedIn(): UserModel?
}