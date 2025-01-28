package com.example.logifitappp.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class MainRoutes {
    @Serializable
    data class DrowsinessTestDetail(val drowsinessTestId: Int): MainRoutes()

    @Serializable
    data object DrowsinessTests: MainRoutes()

    @Serializable
    data class Graphics(val mac: String): MainRoutes()

    @Serializable
    data object HealthInformation: MainRoutes()

    @Serializable
    data class HeartRateDetail(val mac: String): MainRoutes()

    @Serializable
    data class LessonDetail(val lessonId: Int, val serializedLessonIds: String): MainRoutes()

    @Serializable
    data object Notifications: MainRoutes()

    @Serializable
    data object OccupationalInformation: MainRoutes()

    @Serializable
    data class PairingWorker(val mac: String, val serializedWorkers: String): MainRoutes()

    @Serializable
    data object PasswordRecovery: MainRoutes()

    @Serializable
    data object PersonalInformation: MainRoutes()

    @Serializable
    data object Roster: MainRoutes()

    @Serializable
    data object RosterRecording: MainRoutes()

    @Serializable
    data object SleepDataRecording: MainRoutes()

    @Serializable
    data class SleepDetail(val mac: String): MainRoutes()

    @Serializable
    data object SplashScreen: MainRoutes()

    @Serializable
    data class Spo2Detail(val mac: String): MainRoutes()

    @Serializable
    data class StepsDetail(val mac: String): MainRoutes()

    @Serializable
    data class StressDetail(val mac: String): MainRoutes()

    @Serializable
    data object Trainings: MainRoutes()

    @Serializable
    data class TrainingsDetail(val trainingId: Int): MainRoutes()

    @Serializable
    data object WearableDetection: MainRoutes()

    @Serializable
    data class WearableProfile(val mac: String): MainRoutes()

    @Serializable
    data class WearableSettings(val mac: String): MainRoutes()
}