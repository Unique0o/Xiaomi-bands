package com.example.logifitappp.data.repository

import android.content.Context
import com.example.logifitappp.R
import com.example.logifitappp.data.models.TrainingInfoModel
import com.example.logifitappp.domain.repository.TrainingRepository
import javax.inject.Inject

class TrainingRepositoryImpl @Inject constructor(
    private val context: Context
) : TrainingRepository {
    override suspend fun getTrainingInfo(): List<TrainingInfoModel> {
        return listOf(
            TrainingInfoModel("Smartband Logifit", "Logifit cuenta con una smar...", R.drawable.user1),
            TrainingInfoModel("App Logifit", "Conoce en esta capacitació...", R.drawable.user1),
            TrainingInfoModel("Plataforma de Gesti...", "En este módulo podrá desc...", R.drawable.user1),
            TrainingInfoModel("Capacitación sobre F...", "Este módulo está diseñado...", R.drawable.user1),
            TrainingInfoModel("Manual de Procedim...", "Este módulo ofrece una guí...", R.drawable.user1)
        )
    }
}