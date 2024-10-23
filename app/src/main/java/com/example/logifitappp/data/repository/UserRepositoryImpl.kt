package com.example.logifitappp.data.repository

import com.example.logifitappp.data.dao.UserDao
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.data.remote.api.UserApi
import com.example.logifitappp.data.remote.dto.requests.StorePersonalInformationRequest
import com.example.logifitappp.data.remote.dto.response.StorePersonalInformationResponse
import com.example.logifitappp.domain.repository.UserRepository
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val api: UserApi
) : UserRepository {

    override suspend fun isUserAdmin(): Boolean {
        return false
    }

    override suspend fun createOrUpdate(user: UserModel) {
        userDao.store(user)
    }

    override suspend fun store(userId: Int, request: StorePersonalInformationRequest): Response<StorePersonalInformationResponse> {
        return api.storePersonalInformation(userId, request)
    }

    override suspend fun getLoggedIn(): UserModel? {
        return userDao.getLoggedIn()
    }

}