package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.models.HealthInfoModel

interface HealthInfoRepository {
    suspend fun getHealthInfo(): List<HealthInfoModel>
}