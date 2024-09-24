package com.example.logifitappp.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.screens.SplashScreen
import com.example.logifitappp.ui.screens.additionalInformation.AdditionalInformationPicture
import com.example.logifitappp.ui.screens.graphics.GraphicsEmptyScreen
import com.example.logifitappp.ui.screens.home.HomeScreen
import com.example.logifitappp.ui.screens.home.admin.HomeAdminScreen
import com.example.logifitappp.ui.screens.login.LoginView
import com.example.logifitappp.ui.screens.password_recovery.PasswordRecoveryView
import com.example.logifitappp.ui.screens.wearable_detection.WearableDetectionView
import com.example.logifitappp.viewmodel.views.AppViewModel
import com.example.logifitappp.viewmodel.views.LoginViewModel
import com.example.logifitappp.viewmodel.views.graphics.GraphicsViewModel
import com.example.logifitappp.viewmodel.views.home.HomeViewModel

@Composable
fun MainNavigation(
    appViewModel: AppViewModel,
    loginViewModel: LoginViewModel,
    navigation: NavHostController = rememberNavController()
) {
    val isAdmin by loginViewModel.isAdmin.collectAsState()
    Log.d("MainNavigation", "Recomposition triggered. Is admin: $isAdmin")
    NavHost(
        navController = navigation,
        startDestination = MainRoutes.SplashScreen
    ) {
        composable<MainRoutes.Login> { LoginView(navigation) }
        composable<MainRoutes.PasswordRecovery> { PasswordRecoveryView(navigation) }
        composable<MainRoutes.SplashScreen> { SplashScreen(appViewModel, navigation) }
        composable<MainRoutes.WearableDetection> { WearableDetectionView(navigation) }

        composable(
            route = "${MainRoutes.Home::class.simpleName}/{isAdmin}",
            arguments = listOf(navArgument("isAdmin") { type = NavType.BoolType })
        ) { backStackEntry ->
            val isAdmin = backStackEntry.arguments?.getBoolean("isAdmin") ?: false
            if (isAdmin) {
                HomeAdminScreen(navigation)
            } else {
                HomeScreen(HomeViewModel())
            }
        }
        composable<MainRoutes.Graphics> {
            GraphicsEmptyScreen(navigation, GraphicsViewModel())
        }
        composable<MainRoutes.AdditionalInformationPicture> { AdditionalInformationPicture(navigation) }
    }
}