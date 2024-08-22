package com.example.logifitappp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.theme.Blue130
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Stone470

@Composable
fun BottomNavigationBar(
    selectedRoute: MainRoutes,
    onRouteSelected: (MainRoutes) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        NavItem(
            icon = Icons.Default.Home,
            isSelected = selectedRoute is MainRoutes.Home
        ) { onRouteSelected(MainRoutes.Home) }
        NavItem(
            icon = Icons.Default.PieChart,
            isSelected = selectedRoute is MainRoutes.Graphics
        ) { onRouteSelected(MainRoutes.Graphics) }
        NavItem(
            icon = Icons.Default.EmojiEvents,
            isSelected = selectedRoute is MainRoutes.Profile
        ) { onRouteSelected(MainRoutes.Profile) }
        NavItem(
            icon = Icons.Default.Person,
            isSelected = selectedRoute is MainRoutes.Profile
        ) { onRouteSelected(MainRoutes.Profile) }
    }
}

@Composable
fun NavItem(icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (isSelected) Blue130 else Color.Transparent)
            .padding(8.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) Blue690 else Stone470,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview
@Composable
fun NavigationBarPreview() {
   BottomNavigationBar( MainRoutes.Home, onRouteSelected = {  })
}