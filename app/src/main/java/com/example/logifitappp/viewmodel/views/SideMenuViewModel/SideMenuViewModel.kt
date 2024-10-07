package com.example.logifitappp.viewmodel.views.SideMenuViewModel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.lifecycle.ViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.menu.MenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SideMenuViewModel @Inject constructor() : ViewModel() {
    private var _navController: NavHostController? = null

    private val _isMenuOpen = MutableStateFlow(false)
    val isMenuOpen: StateFlow<Boolean> = _isMenuOpen.asStateFlow()

    private val _menuItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val menuItems: StateFlow<List<MenuItem>> = _menuItems.asStateFlow()

    init {
        initializeMenuItems()
    }

    fun setNavController(controller: NavHostController) {
        _navController = controller
    }

    private fun initializeMenuItems() {
        _menuItems.value = listOf(
            MenuItem(Icons.Default.Person, "Información Ocupacional") { navigateTo(MainRoutes.OccupationalInformation) },
            MenuItem(Icons.Default.Work, "Información Personal") { navigateTo(MainRoutes.PersonalInformation) },
            MenuItem(Icons.Default.Favorite, "Informaciòn de salud") { navigateTo(MainRoutes.HealthInformation) },
            MenuItem(Icons.Default.Favorite, "Capacitaciones") { navigateTo(MainRoutes.Trainings) },
            MenuItem(Icons.Default.Help, "Notificaciones") { navigateTo(MainRoutes.Notifications) },
            MenuItem(Icons.Default.ExitToApp, "Cerrar sesión") { logout() }
        )
    }

    fun openMenu() {
        _isMenuOpen.value = true
    }

    fun closeMenu() {
        _isMenuOpen.value = false
    }

    private fun navigateTo(route: MainRoutes) {
        _navController?.let { navController ->
            navController.navigate(route::class.simpleName!!) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }

                launchSingleTop = true
                restoreState = true
            }
            closeMenu()
        }
    }

    private fun logout() {
        _navController?.let { navController ->
            // TODO: Implementar la lógica de cierre de sesión
            navController.navigate(MainRoutes.Login::class.simpleName!!) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
            }
            closeMenu()
        }
    }
}