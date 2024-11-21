package com.example.logifitappp.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.screens.BottomTabScreen
import com.example.logifitappp.ui.screens.SplashScreen
import com.example.logifitappp.ui.screens.AddSleepData.AddSleepDataView
import com.example.logifitappp.ui.screens.login.LoginView
import com.example.logifitappp.ui.screens.password_recovery.PasswordRecoveryView
import com.example.logifitappp.ui.screens.wearable_detection.WearableDetectionView
import com.example.logifitappp.viewmodel.views.AppViewModel

@Composable
fun MainNavigation(
    appViewModel: AppViewModel,
    navigation: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navigation,
        startDestination = MainRoutes.SplashScreen
    ) {
        composable<MainRoutes.BottomTabsNavigation> { BottomTabScreen(appViewModel, navigation) }
        composable<MainRoutes.Login> { LoginView(appViewModel, navigation) }
        composable<MainRoutes.PasswordRecovery> { PasswordRecoveryView(navigation) }
        composable<MainRoutes.SplashScreen> { SplashScreen(appViewModel, navigation) }
        composable<MainRoutes.WearableDetection> { WearableDetectionView(navigation) }
        composable<MainRoutes.AddSleepData> { AddSleepDataView(navigation) }
    }
}