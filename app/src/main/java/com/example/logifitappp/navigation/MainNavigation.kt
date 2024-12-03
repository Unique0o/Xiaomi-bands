package com.example.logifitappp.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.screens.SplashScreen
import com.example.logifitappp.ui.screens.lesson_detail.LessonDetailView
import com.example.logifitappp.ui.screens.notifications.NotificationsView
import com.example.logifitappp.ui.screens.password_recovery.PasswordRecoveryView
import com.example.logifitappp.ui.screens.roster.RosterView
import com.example.logifitappp.ui.screens.roster_recording.RosterRecordingView
import com.example.logifitappp.ui.screens.sleep_data_recording.SleepDataRecordingView
import com.example.logifitappp.ui.screens.training_detail.TrainingDetailView
import com.example.logifitappp.ui.screens.trainings.TrainingsView
import com.example.logifitappp.ui.screens.wearable_detection.WearableDetectionView
import com.example.logifitappp.ui.screens.wearable_profile.WearableProfileView
import com.example.logifitappp.viewmodel.AppViewModel

@Composable
fun MainNavigation(
    appViewModel: AppViewModel,
    navigation: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navigation,
        startDestination = MainRoutes.SplashScreen
    ) {
        composable<MainRoutes.Notifications> { NotificationsView(navigation) }
        composable<MainRoutes.PasswordRecovery> { PasswordRecoveryView(navigation) }
        composable<MainRoutes.Roster> { RosterView(appViewModel, navigation) }
        composable<MainRoutes.RosterRecording> { RosterRecordingView(appViewModel, navigation) }
        composable<MainRoutes.SleepDataRecording> { SleepDataRecordingView(navigation) }
        composable<MainRoutes.SplashScreen> { SplashScreen(appViewModel, navigation) }
        composable<MainRoutes.Trainings> { TrainingsView(navigation) }
        composable<MainRoutes.WearableDetection> { WearableDetectionView(navigation) }

        composable<MainRoutes.LessonDetail> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<MainRoutes.LessonDetail>()

            LessonDetailView(appViewModel, navigation, arguments.lessonId)
        }

        composable<MainRoutes.TrainingsDetail> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<MainRoutes.TrainingsDetail>()

            TrainingDetailView(navigation, arguments.trainingId)
        }

        composable<MainRoutes.WearableProfile> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<MainRoutes.WearableProfile>()

            WearableProfileView(appViewModel, navigation, arguments.mac)
        }
    }
}