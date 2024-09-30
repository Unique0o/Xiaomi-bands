package com.example.logifitappp.ui.components.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.logifitappp.R
import com.example.logifitappp.viewmodel.views.SideMenuViewModel.SideMenuViewModel

@Composable
fun SideMenuScreen(

) {
    val viewModel: SideMenuViewModel = hiltViewModel()
    val isMenuOpen by viewModel.isMenuOpen.collectAsState()
    val menuItems by viewModel.menuItems.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Button(onClick = { viewModel.openMenu() }) {
            Text("Abrir Menú")
        }

        SideMenu(
            isOpen = isMenuOpen,
            onClose = { viewModel.closeMenu() },
            name = "CARLOS ABAD",
            role = "Operador en LOGIFIT",
            avatarResId = R.drawable.user1,
            menuItems = menuItems
        )
    }
}

