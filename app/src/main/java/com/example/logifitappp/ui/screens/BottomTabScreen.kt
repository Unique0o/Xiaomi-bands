package com.example.logifitappp.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.navigation.routes.BottomTabRoutes
import com.example.logifitappp.ui.screens.login.LoginView
import com.example.logifitappp.ui.screens.password_recovery.PasswordRecoveryView
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.viewmodel.BottomTabScreenViewModel
import com.example.logifitappp.viewmodel.views.AppViewModel

@Composable
fun BottomTabScreen(
    appViewModel: AppViewModel,
    navigation: NavHostController
) {
    val bottomTabNavigation = rememberNavController()
    val bottomTabScreenViewModel: BottomTabScreenViewModel = viewModel()

    val screens = bottomTabScreenViewModel.getScreens(appViewModel.user!!)

    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier
                    .navigationBarsPadding()
                    .height(64.dp)
            ) {
                screens.forEachIndexed { index, item ->
                    val iconSize = 25
                    val isSelected = bottomTabScreenViewModel.index == index
                    val duration = tween<Float>(durationMillis = 500)

                    val iconAlpha by animateFloatAsState(
                        animationSpec = duration,
                        label = "icon alpha",
                        targetValue = if (isSelected) 1f else 0.5f
                    )

                    val animatedRadius by animateFloatAsState(
                        animationSpec = duration,
                        label = "expanded radius",
                        targetValue = if (isSelected) iconSize + 16f else 0f
                    )

                    BottomNavigationItem(
                        alwaysShowLabel = false,
                        icon = {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Canvas(modifier = Modifier.size((iconSize + 16).dp)) {
                                    drawCircle(
                                        color = Blue690.copy(alpha = iconAlpha),
                                        radius = animatedRadius
                                    )
                                }

                                Icon(
                                    contentDescription = null,
                                    imageVector = item.icon,
                                    modifier = Modifier.size(iconSize.dp),
                                    tint = if (isSelected) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = iconAlpha)
                                )
                            }
                        },
                        label = null,
                        selected = isSelected,
                        onClick = {
                            bottomTabScreenViewModel.index = index

                            bottomTabNavigation.navigate(item.route) {
                                popUpTo(bottomTabNavigation.graph.startDestinationId) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(bottomTabNavigation, modifier = Modifier.padding(innerPadding), startDestination = BottomTabRoutes.Home) {
            composable<BottomTabRoutes.Home> { LoginView(appViewModel, navigation) }
            composable<BottomTabRoutes.Graphics> { PasswordRecoveryView(navigation) }
        }
    }
}