package com.example.logifitappp.domain.usecase

import com.example.logifitappp.data.remote.dto.response.WorkerResponse
import com.example.logifitappp.domain.service.AdminService
import javax.inject.Inject


class AdminUseCase @Inject constructor(private val adminService: AdminService) {
    suspend operator fun invoke(): WorkerResponse {
        return adminService.fetchWorkers()
    }
}