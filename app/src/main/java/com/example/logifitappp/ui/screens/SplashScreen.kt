package com.example.logifitappp.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.logifitappp.ui.screens.login.LoginView
import com.example.logifitappp.ui.screens.onboarding.OnboardingView
import com.example.logifitappp.ui.screens.unauthorized.UnauthorizedView
import com.example.logifitappp.viewmodel.AppViewModel

@Composable
fun SplashScreen(
    appViewModel: AppViewModel,
    navigation: NavHostController
) {
    when {
        appViewModel.user == null -> LoginView(appViewModel, navigation)
        !appViewModel.shouldItOmitOnboarding -> OnboardingView(appViewModel)
        appViewModel.user?.isActive == false || appViewModel.tenant?.isActive == false -> UnauthorizedView(appViewModel)
        else -> BottomTabScreen(appViewModel, navigation)
    }
}