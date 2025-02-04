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
            HealthInfoModel(context.getString(R.string.weight), "-", "kg", R.drawable.weigth),
            HealthInfoModel(context.getString(R.string.height), "-", context.getString(R.string.meters), R.drawable.height),
            HealthInfoModel(context.getString(R.string.blood_type), "", "", R.drawable.blood),
            HealthInfoModel(context.getString(R.string.gender), "", "", R.drawable.geneder)
        )
    }
}