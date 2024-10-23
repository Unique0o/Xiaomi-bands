package com.example.logifitappp.domain.usecase

import com.example.logifitappp.data.models.HealthInfoModel
import com.example.logifitappp.domain.repository.HealthInfoRepository
import javax.inject.Inject

class HealthInfoUseCase @Inject constructor(private val repository: HealthInfoRepository) {
    suspend operator fun invoke(): List<HealthInfoModel> { return repository.getHealthInfo() }
}