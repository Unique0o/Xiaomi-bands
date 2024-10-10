package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.models.LessonModel
import com.example.logifitappp.data.models.TrainingInfoModel

interface TrainingRepository {
    suspend fun getTrainingInfo(): List<TrainingInfoModel>
    suspend fun getTrainingLessons(trainingId: String): List<LessonModel>
}