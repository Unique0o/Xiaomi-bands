package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.api.AdminApi
import com.example.logifitappp.data.remote.dto.response.WorkerResponse
import com.example.logifitappp.domain.repository.AdminRepository
import retrofit2.Response
import javax.inject.Inject

class AdminRepositoryImpl @Inject constructor(private val adminApi: AdminApi): AdminRepository {
    override suspend fun fetchWorkers(): Response<WorkerResponse> {
        return adminApi.fetchWorkers()
    }
}