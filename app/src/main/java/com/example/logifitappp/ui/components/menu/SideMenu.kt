package com.example.logifitappp.ui.components.menu

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.data.models.MenuItem
import com.example.logifitappp.ui.components.items.MenuItemComponent
import com.example.logifitappp.ui.theme.LogifitApppTheme


@Composable
fun SideMenu(
    isOpen: Boolean,
    onClose: () -> Unit,
    name: String,
    role: String,
    avatarResId: Int,
    menuItems: List<MenuItem>
) {
    val offsetX by animateDpAsState(
        targetValue = if (isOpen) 0.dp else -300.dp,
        animationSpec = tween(durationMillis = 300)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        if (isOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray.copy(alpha = 0.5f))
                    .clickable(onClick = onClose)
            )
        }

        Box(
            modifier = Modifier
                .offset(x = offsetX)
                .width(300.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column {
                ProfileHeader(name = name, role = role, avatarResId = avatarResId)
                HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                LazyColumn {
                    items(menuItems) { item ->
                        MenuItemComponent(item = item)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MyScreen() {
    LogifitApppTheme {
        SideMenuScreen(navigation = rememberNavController())
    }

}