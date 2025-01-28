package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.response.WorkerResponse
import retrofit2.Response

interface AdminRepository {
    suspend fun fetchWorkers(): Response<WorkerResponse>
}