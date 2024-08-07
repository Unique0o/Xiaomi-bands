package com.example.logifitappp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.screens.login.LoginView

@Composable
fun MainNavigation(navigation: NavHostController) {
    NavHost(
        navController = navigation,
        startDestination = MainRoutes.Login
    ) {
        composable<MainRoutes.Login> { LoginView(navigation) }
    }
}