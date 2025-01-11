package com.example.logifitappp.ui.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.core.utils.avoidTop
import com.example.logifitappp.core.wearebles.WearableManager
import com.example.logifitappp.navigation.routes.BottomTabRoutes
import com.example.logifitappp.ui.components.SideBarContent
import com.example.logifitappp.ui.screens.graphics.GraphicsView
import com.example.logifitappp.ui.screens.home.HomeView
import com.example.logifitappp.ui.screens.meditation.MeditationView
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.viewmodel.views.BottomTabScreenViewModel
import com.example.logifitappp.viewmodel.AppViewModel

@Composable
fun BottomTabScreen(
    appViewModel: AppViewModel,
    navigation: NavHostController
) {
    val user = appViewModel.user

    val bottomTabNavigation = rememberNavController()
    val bottomTabScreenViewModel = hiltViewModel<BottomTabScreenViewModel, BottomTabScreenViewModel.BottomTabScreenViewModelFactory>{
        it.create(user)
    }

    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    DisposableEffect(Unit) {
        val receiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                when (intent.action) {
                    WearableManager.ACTION_DEVICES_CHANGED -> {
                        bottomTabScreenViewModel.refreshPairedWearables()
                    }
                }
            }
        }

        val filterLocal = IntentFilter()
        filterLocal.addAction(WearableManager.ACTION_DEVICES_CHANGED)
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filterLocal)

        onDispose {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .fillMaxHeight()
            ) {
                SideBarContent(
                    appViewModel = appViewModel,
                    drawerState = drawerState,
                    navigation = navigation
                )
            }
        },
        drawerState = drawerState,
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                    tonalElevation = 0.dp
                ) {
                    bottomTabScreenViewModel.screens.forEachIndexed { index, item ->
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

                        NavigationBarItem(
                            alwaysShowLabel = false,
                            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
                            icon = {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.height(70.dp)
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
            NavHost(bottomTabNavigation, startDestination = BottomTabRoutes.Home) {
                composable<BottomTabRoutes.Home> { HomeView(appViewModel, drawerState, navigation, innerPadding.avoidTop()) }
                composable<BottomTabRoutes.Graphics> { GraphicsView(appViewModel, drawerState, navigation, innerPadding.avoidTop()) }
                composable<BottomTabRoutes.Meditation> { MeditationView(appViewModel, drawerState, navigation, innerPadding.avoidTop()) }
            }
        }
    }
}