package com.example.logifitappp.viewmodel.views.SideMenuViewModel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.lifecycle.ViewModel
import com.example.logifitappp.ui.components.menu.MenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SideMenuViewModel  @Inject constructor(): ViewModel()  {
    private val _isMenuOpen = MutableStateFlow(false)
    val isMenuOpen: StateFlow<Boolean> = _isMenuOpen.asStateFlow()

    private val _menuItems = MutableStateFlow(listOf(
        MenuItem(Icons.Default.Person, "Información personal") { /* Action */ },
        MenuItem(Icons.Default.Work, "Información laboral") { /* Action */ },
        MenuItem(Icons.Default.Favorite, "Información de salud") { /* Action */ },
        MenuItem(Icons.Default.Help, "Ayuda") { /* Action */ },
        MenuItem(Icons.Default.Description, "Términos y condiciones") { /* Action */ },
        MenuItem(Icons.Default.ExitToApp, "Cerrar sesión") { /* Action */ }
    ))
    val menuItems: StateFlow<List<MenuItem>> = _menuItems.asStateFlow()

    fun openMenu() {
        _isMenuOpen.value = true
    }

    fun closeMenu() {
        _isMenuOpen.value = false
    }

}