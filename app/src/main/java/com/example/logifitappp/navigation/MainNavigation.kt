package com.example.logifitappp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.screens.graphics.GraphicsEmptyScreen
import com.example.logifitappp.ui.screens.home.admin.HomeAdminScreen
import com.example.logifitappp.ui.screens.login.LoginView
import com.example.logifitappp.ui.screens.password_recovery.PasswordRecoveryView
import com.example.logifitappp.ui.screens.wearable_detection.WearableDetectionView

@Composable
fun MainNavigation(navigation: NavHostController) {
    NavHost(
        navController = navigation,
        startDestination = MainRoutes.Login
    ) {
        composable<MainRoutes.Login> { LoginView(navigation) }
        composable<MainRoutes.PasswordRecovery> { PasswordRecoveryView(navigation) }
        composable<MainRoutes.WearableDetection> { WearableDetectionView(navigation) }

        composable<MainRoutes.Home> {
            HomeAdminScreen(navigation)
        }
        composable<MainRoutes.Graphics> {
            GraphicsEmptyScreen(navigation)
        }
    }
}