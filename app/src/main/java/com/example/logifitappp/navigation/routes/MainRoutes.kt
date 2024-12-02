package com.example.logifitappp.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class MainRoutes {
    @Serializable
    data object BottomTabsNavigation: MainRoutes()

    @Serializable
    data object HealthInformation: MainRoutes()

    @Serializable
    data class LessonDetail(val lessonId: Int): MainRoutes()

    @Serializable
    data object Login: MainRoutes()

    @Serializable
    data object Notifications: MainRoutes()

    @Serializable
    data object OccupationalInformation: MainRoutes()

    @Serializable
    data object Onboarding: MainRoutes()

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
    data object SplashScreen: MainRoutes()

    @Serializable
    data object Trainings: MainRoutes()

    @Serializable
    data class TrainingsDetail(val trainingId: Int): MainRoutes()

    @Serializable
    data object WearableDetection: MainRoutes()

    @Serializable
    data class WearableProfile(val mac: String): MainRoutes()



    @Serializable
    data object Graphics : MainRoutes() //

    @Serializable
    data object Home : MainRoutes() //

    @Serializable
    data object Profile : MainRoutes()

    @Serializable
    data object AdditionalInformationPicture : MainRoutes()

    @Serializable
    data object AppLanguage : MainRoutes()

    @Serializable
    data object Help : MainRoutes()

    @Serializable
    data object TermsAndConditions : MainRoutes()
}