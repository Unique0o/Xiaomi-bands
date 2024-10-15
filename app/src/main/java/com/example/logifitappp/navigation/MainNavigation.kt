package com.example.logifitappp.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.logifitappp.ui.screens.Trainings.TrainingDetailScreen
import com.example.logifitappp.ui.screens.Trainings.TrainingsScreen
import com.example.logifitappp.ui.screens.additionalInformation.AdditionalInformationPicture
import com.example.logifitappp.ui.screens.appLanguage.AppLanguageScreen
import com.example.logifitappp.ui.screens.graphics.GraphicsEmptyScreen
import com.example.logifitappp.ui.screens.healthInfo.HealthInfoScreen
import com.example.logifitappp.ui.screens.help.HelpScreen
import com.example.logifitappp.ui.screens.home.HomeScreen
import com.example.logifitappp.ui.screens.home.admin.HomeAdminScreen
import com.example.logifitappp.ui.screens.login.LoginView
import com.example.logifitappp.ui.screens.occupationalInfo.OccupationalInfoScreen
import com.example.logifitappp.ui.screens.password_recovery.PasswordRecoveryView
import com.example.logifitappp.ui.screens.personalInformation.PersonalInfoScreen
import com.example.logifitappp.ui.screens.termsConditions.TermsAndConditionsScreen
import com.example.logifitappp.ui.screens.wearable_detection.WearableDetectionView
import com.example.logifitappp.viewmodel.views.AppViewModel
import com.example.logifitappp.viewmodel.views.LoginViewModel
import com.example.logifitappp.viewmodel.views.SideMenuViewModel.SideMenuViewModel
import com.example.logifitappp.viewmodel.views.graphics.GraphicsViewModel
import com.example.logifitappp.viewmodel.views.home.HomeViewModel

@Composable
fun MainNavigation(
    appViewModel: AppViewModel,
    loginViewModel: LoginViewModel,
    navigation: NavHostController = rememberNavController()
) {
    val isAdmin by loginViewModel.isAdmin.collectAsState()

    NavHost(
        navController = navigation,
        startDestination = MainRoutes.SplashScreen
    ) {
        composable<MainRoutes.Login> { LoginView(navigation) }
        composable<MainRoutes.PasswordRecovery> { PasswordRecoveryView(navigation) }
        composable<MainRoutes.SplashScreen> { SplashScreen(appViewModel, navigation) }
        composable<MainRoutes.WearableDetection> { WearableDetectionView(navigation) }
        composable(MainRoutes.OccupationalInformation::class.simpleName!!) { OccupationalInfoScreen(navigation) }
        composable(MainRoutes.PersonalInformation::class.simpleName!!) { PersonalInfoScreen(navigation) }
        composable(MainRoutes.TermsAndConditions::class.simpleName!!) { TermsAndConditionsScreen(navigation) }
        composable(
            route = MainRoutes.TrainingsDetail::class.simpleName!! + "/{trainingId}",
            arguments = listOf(navArgument("trainingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val trainingId = backStackEntry.arguments?.getString("trainingId") ?: ""
            TrainingDetailScreen(navigation, trainingId)
        }
        composable(MainRoutes.Trainings::class.simpleName!!) { TrainingsScreen(navigation) }
        composable(MainRoutes.HealthInformation::class.simpleName!!) { HealthInfoScreen(navigation) }
        composable(MainRoutes.AppLanguage::class.simpleName!!) { AppLanguageScreen(navigation) }
        composable(MainRoutes.Help::class.simpleName!!) { HelpScreen(navigation) }

        composable(MainRoutes.Home::class.simpleName!!) {
            val sideMenuViewModel = androidx.lifecycle.viewmodel.compose.viewModel<SideMenuViewModel>()
            LaunchedEffect(navigation) {
                sideMenuViewModel.setNavController(navigation)
            }
            HomeScreen(HomeViewModel(), sideMenuViewModel, navigation)
        }

        composable(
            route = "${MainRoutes.Home::class.simpleName}/{isAdmin}",
            arguments = listOf(navArgument("isAdmin") { type = NavType.BoolType })
        ) { backStackEntry ->
            val isAdmin = backStackEntry.arguments?.getBoolean("isAdmin") ?: false
            if (isAdmin) {
                HomeAdminScreen(navigation)
            } else {
                val sideMenuViewModel = androidx.lifecycle.viewmodel.compose.viewModel<SideMenuViewModel>()
                LaunchedEffect(navigation) {
                    sideMenuViewModel.setNavController(navigation)
                }
                HomeScreen(HomeViewModel(), sideMenuViewModel, navigation)
            }
        }
        composable<MainRoutes.Graphics> {
            GraphicsEmptyScreen(navigation, GraphicsViewModel())
        }
        composable<MainRoutes.AdditionalInformationPicture> { AdditionalInformationPicture(navigation) }
    }
}