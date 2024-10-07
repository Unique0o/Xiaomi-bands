package com.example.logifitappp.data.repository

import android.content.Context
import com.example.logifitappp.R
import com.example.logifitappp.data.models.HealthInfoModel
import com.example.logifitappp.domain.repository.HealthInfoRepository
import javax.inject.Inject

class HealthInfoRepositoryImpl @Inject constructor(
    private val context: Context
) : HealthInfoRepository {
    override suspend fun getHealthInfo(): List<HealthInfoModel> {
        return listOf(
            HealthInfoModel("Weight", "-", "kg", R.drawable.user1),
            HealthInfoModel("Height", "-", "meters", R.drawable.user1),
            HealthInfoModel("Blood type", "B-", "", R.drawable.user1),
            HealthInfoModel("Gender", "male", "", R.drawable.user1)
        )
    }
}