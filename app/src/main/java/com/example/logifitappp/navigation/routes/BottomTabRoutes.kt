package com.example.logifitappp.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class BottomTabRoutes {
    @Serializable
    data object AllInOne: BottomTabRoutes()

    @Serializable
    data object Graphics: BottomTabRoutes()

    @Serializable
    data object Home: BottomTabRoutes()

    @Serializable
    data object Meditation: BottomTabRoutes()

    @Serializable
    data object SynchronizationReport: BottomTabRoutes()
}