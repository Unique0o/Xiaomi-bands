package com.example.logifitappp.ui.components.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.viewmodel.views.SideMenuViewModel.SideMenuViewModel

@Composable
fun SideMenuScreen(
  navigation: NavHostController
) {
    val viewModel: SideMenuViewModel = hiltViewModel()


    LaunchedEffect(Unit) {
        viewModel.setNavController(navigation)
    }
    val isMenuOpen by viewModel.isMenuOpen.collectAsState()
    val menuItems by viewModel.menuItems.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {


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

