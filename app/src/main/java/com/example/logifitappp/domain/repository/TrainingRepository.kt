package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.models.TrainingInfoModel

interface TrainingRepository {
    suspend fun getTrainingInfo(): List<TrainingInfoModel>
}