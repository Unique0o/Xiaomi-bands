package com.example.logifitappp.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.screens.login.LoginView
import com.example.logifitappp.viewmodel.views.AppViewModel

@Composable
fun SplashScreen(
    appViewModel: AppViewModel,
    navigation: NavHostController
) {
    if (appViewModel.user == null) {
        LoginView(appViewModel, navigation)
    } else BottomTabScreen(appViewModel, navigation)
}