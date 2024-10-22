package com.example.logifitappp.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.logifitappp.navigation.routes.BottomTabRoutes

enum class BottomTabScreenEnum(val route: BottomTabRoutes, val icon: ImageVector) {
    HOME(BottomTabRoutes.Home, Icons.Filled.Home),
    GRAPHICS(BottomTabRoutes.Graphics, Icons.Filled.PieChart);
}