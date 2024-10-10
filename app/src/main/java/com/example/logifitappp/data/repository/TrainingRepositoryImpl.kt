package com.example.logifitappp.data.repository

import android.content.Context
import com.example.logifitappp.R
import com.example.logifitappp.data.models.LessonModel
import com.example.logifitappp.data.models.TrainingInfoModel
import com.example.logifitappp.domain.repository.TrainingRepository
import javax.inject.Inject

class TrainingRepositoryImpl @Inject constructor(
    private val context: Context
) : TrainingRepository {
    override suspend fun getTrainingInfo(): List<TrainingInfoModel> {
        return listOf(
            TrainingInfoModel("1", "Smartband Logifit", "Logifit cuenta con una smar...", R.drawable.user1),
            TrainingInfoModel("2", "App Logifit", "Conoce en esta capacitació...", R.drawable.user1),
            TrainingInfoModel("3", "Plataforma de Gesti...", "En este módulo podrá desc...", R.drawable.user1),
            TrainingInfoModel("4", "Capacitación sobre F...", "Este módulo está diseñado...", R.drawable.user1),
            TrainingInfoModel("5", "Manual de Procedim...", "Este módulo ofrece una guí...", R.drawable.user1)
        )
    }

    override suspend fun getTrainingLessons(trainingId: String): List<LessonModel> {
        return listOf(
            LessonModel(
                id = 1,
                description = "Introducción a las pulseras inteligentes",
                duration = 88,
                externalIdentifier = 101,
                isCompleted = false,
                name = "Qué es una Smartband",
                path = "/lessons/smartband_intro",
                trainingExternalIdentifier = 1001,
                videoUrl = "https://example.com/videos/smartband_intro.mp4",
                imageRes = R.drawable.user1
            ),
            LessonModel(
                id = 2,
                description = "Cómo configurar tu Smartband Logifit",
                duration = 135,
                externalIdentifier = 102,
                isCompleted = false,
                name = "Configuración inicial",
                path = "/lessons/smartband_setup",
                trainingExternalIdentifier = 1001,
                videoUrl = "https://example.com/videos/smartband_setup.mp4",
                imageRes = R.drawable.user1
            ),
            LessonModel(
                id = 3,
                description = "Aprende a monitorear tu actividad diaria",
                duration = 115,
                externalIdentifier = 103,
                isCompleted = false,
                name = "Seguimiento de actividad",
                path = "/lessons/activity_tracking",
                trainingExternalIdentifier = 1001,
                videoUrl = "https://example.com/videos/activity_tracking.mp4",
                imageRes = R.drawable.user1
            )
        )
    }
}