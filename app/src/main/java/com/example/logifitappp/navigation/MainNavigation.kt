package com.example.logifitappp.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.logifitappp.data.remote.dto.response.WorkerItemResponse
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.screens.SplashScreen
import com.example.logifitappp.ui.screens.drowsiness_test_detail.DrowsinessTestDetailView
import com.example.logifitappp.ui.screens.drowsiness_tests.DrowsinessTestsView
import com.example.logifitappp.ui.screens.graphics.GraphicsView
import com.example.logifitappp.ui.screens.heart_rate_detail.HeartRateDetailView
import com.example.logifitappp.ui.screens.lesson_detail.LessonDetailView
import com.example.logifitappp.ui.screens.notifications.NotificationsView
import com.example.logifitappp.ui.screens.occupational_information.OccupationalInformationView
import com.example.logifitappp.ui.screens.pairing_worker.PairingWorkerView
import com.example.logifitappp.ui.screens.password_recovery.PasswordRecoveryView
import com.example.logifitappp.ui.screens.personal_information.PersonalInformationView
import com.example.logifitappp.ui.screens.roster.RosterView
import com.example.logifitappp.ui.screens.roster_recording.RosterRecordingView
import com.example.logifitappp.ui.screens.sleep_data_recording.SleepDataRecordingView
import com.example.logifitappp.ui.screens.sleep_detail.SleepDetailView
import com.example.logifitappp.ui.screens.spo2_detail.Spo2DetailView
import com.example.logifitappp.ui.screens.steps_detail.StepsDetailView
import com.example.logifitappp.ui.screens.stress_detail.StressDetailView
import com.example.logifitappp.ui.screens.training_detail.TrainingDetailView
import com.example.logifitappp.ui.screens.trainings.TrainingsView
import com.example.logifitappp.ui.screens.wearable_detection.WearableDetectionView
import com.example.logifitappp.ui.screens.wearable_profile.WearableProfileView
import com.example.logifitappp.ui.screens.wearable_settings.WearableSettingsView
import com.example.logifitappp.viewmodel.AppViewModel
import com.google.gson.Gson

@Composable
fun MainNavigation(
    appViewModel: AppViewModel,
    navigation: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navigation,
        startDestination = MainRoutes.SplashScreen
    ) {
        composable<MainRoutes.DrowsinessTests> { DrowsinessTestsView(navigation) }
        composable<MainRoutes.Notifications> { NotificationsView(navigation) }
        composable<MainRoutes.PasswordRecovery> { PasswordRecoveryView(navigation) }
        composable<MainRoutes.PersonalInformation> { PersonalInformationView(appViewModel,navigation) }
        composable<MainRoutes.OccupationalInformation> { OccupationalInformationView(navigation) }
        composable<MainRoutes.Roster> { RosterView(appViewModel, navigation) }
        composable<MainRoutes.RosterRecording> { RosterRecordingView(appViewModel, navigation) }
        composable<MainRoutes.SleepDataRecording> { SleepDataRecordingView(navigation) }
        composable<MainRoutes.SplashScreen> { SplashScreen(appViewModel, navigation) }
        composable<MainRoutes.Trainings> { TrainingsView(navigation) }
        composable<MainRoutes.WearableDetection> { WearableDetectionView(appViewModel, navigation) }

        composable<MainRoutes.DrowsinessTestDetail> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<MainRoutes.DrowsinessTestDetail>()

            DrowsinessTestDetailView(appViewModel, navigation, arguments.drowsinessTestId)
        }

        composable<MainRoutes.Graphics> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<MainRoutes.Graphics>()

            GraphicsView(appViewModel, null, navigation, mac = arguments.mac)
        }

        composable<MainRoutes.HeartRateDetail> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<MainRoutes.HeartRateDetail>()

            HeartRateDetailView(navigation, arguments.mac)
        }

        composable<MainRoutes.LessonDetail> { navBackStackEntry ->
            val gson = Gson()
            val arguments = navBackStackEntry.toRoute<MainRoutes.LessonDetail>()

            LessonDetailView(appViewModel, navigation, arguments.lessonId, gson.fromJson(arguments.serializedLessonIds, Array<Int>::class.java))
        }

        composable<MainRoutes.PairingWorker> { navBackStackEntry ->
            val gson = Gson()
            val arguments = navBackStackEntry.toRoute<MainRoutes.PairingWorker>()

            PairingWorkerView(appViewModel, navigation, arguments.mac, gson.fromJson(arguments.serializedWorkers, Array<WorkerItemResponse>::class.java))
        }

        composable<MainRoutes.SleepDetail> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<MainRoutes.SleepDetail>()

            SleepDetailView(appViewModel, navigation, arguments.mac)
        }

        composable<MainRoutes.Spo2Detail> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<MainRoutes.Spo2Detail>()

            Spo2DetailView(navigation, arguments.mac)
        }

        composable<MainRoutes.StepsDetail> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<MainRoutes.StepsDetail>()

            StepsDetailView(navigation, arguments.mac)
        }

        composable<MainRoutes.StressDetail> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<MainRoutes.StressDetail>()

            StressDetailView(navigation, arguments.mac)
        }

        composable<MainRoutes.TrainingsDetail> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<MainRoutes.TrainingsDetail>()

            TrainingDetailView(navigation, arguments.trainingId)
        }

        composable<MainRoutes.WearableProfile> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<MainRoutes.WearableProfile>()

            WearableProfileView(appViewModel, navigation, arguments.mac)
        }

        composable<MainRoutes.WearableSettings> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<MainRoutes.WearableSettings>()

            WearableSettingsView(navigation, arguments.mac)
        }
    }
}