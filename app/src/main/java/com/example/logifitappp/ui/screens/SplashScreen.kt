package com.example.logifitappp.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.viewmodel.views.AppViewModel

@Composable
fun SplashScreen(
    appViewModel: AppViewModel,
    navigation: NavHostController
) {
    println("splash screen")

    if (appViewModel.user == null) {
        navigation.navigate(MainRoutes.Login) {
            popUpTo(0)
        }
    }
}