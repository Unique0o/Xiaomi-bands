package com.example.logifitappp.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class MainRoutes {
    @Serializable
    data object Login: MainRoutes()

    @Serializable
    data object PasswordRecovery: MainRoutes()

    @Serializable
    data object WearableDetection: MainRoutes()
}