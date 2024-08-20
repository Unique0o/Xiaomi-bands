package com.example.logifitappp.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class MainRoutes {
    @Serializable
    data object Login: MainRoutes()

    @Serializable
    data object Onboarding: MainRoutes()

    @Serializable
    data object PasswordRecovery: MainRoutes()

    @Serializable
    data object Home : MainRoutes()

    @Serializable
    data object Graphics : MainRoutes()

    @Serializable
    data object Profile : MainRoutes()
}