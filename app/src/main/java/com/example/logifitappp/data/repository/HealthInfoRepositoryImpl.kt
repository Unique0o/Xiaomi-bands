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
            HealthInfoModel(context.getString(R.string.weight), "-", "kg", R.drawable.user1),
            HealthInfoModel(context.getString(R.string.height), "-", context.getString(R.string.meters), R.drawable.user1),
            HealthInfoModel(context.getString(R.string.blood_type), "B-", "", R.drawable.user1),
            HealthInfoModel(context.getString(R.string.gender), context.getString(R.string.male), "", R.drawable.user1)
        )
    }
}