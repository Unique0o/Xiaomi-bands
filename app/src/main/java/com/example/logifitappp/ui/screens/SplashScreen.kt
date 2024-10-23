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
    navigation.navigate(if (appViewModel.user == null) MainRoutes.Login else MainRoutes.BottomTabsNavigation) {
        popUpTo(0)
    }
}