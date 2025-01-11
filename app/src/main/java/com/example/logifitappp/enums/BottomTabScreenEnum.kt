package com.example.logifitappp.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Spa
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.logifitappp.navigation.routes.BottomTabRoutes

enum class BottomTabScreenEnum(val route: BottomTabRoutes, val icon: ImageVector) {
    GRAPHICS(BottomTabRoutes.Graphics, Icons.Filled.PieChart),
    HOME(BottomTabRoutes.Home, Icons.Filled.Home),
    MEDITATION(BottomTabRoutes.Meditation, Icons.Default.Spa);
}