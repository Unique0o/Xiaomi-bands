package com.example.logifitappp.viewmodel.views.SideMenuViewModel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.lifecycle.ViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.data.models.MenuItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SideMenuViewModel @Inject constructor(
) : ViewModel() {

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
            MenuItem(Icons.Default.Person, R.string.personal_info) { navigateTo(MainRoutes.PersonalInformation) },
            MenuItem(Icons.Default.Work, R.string.occupational_info) { navigateTo(MainRoutes.OccupationalInformation) },
            MenuItem(Icons.Default.Favorite, R.string.health_info) { navigateTo(MainRoutes.HealthInformation) },
            MenuItem(Icons.AutoMirrored.Filled.MenuBook, R.string.trainings) { navigateTo(MainRoutes.Trainings) },
            MenuItem(Icons.Default.Translate, R.string.language) { navigateTo(MainRoutes.AppLanguage) },
            MenuItem(Icons.AutoMirrored.Filled.Help, R.string.help) { navigateTo(MainRoutes.Help) },
            MenuItem(Icons.Default.PrivacyTip, R.string.terms_and_conditions) { navigateTo(MainRoutes.Notifications) },
            MenuItem(Icons.AutoMirrored.Filled.Logout, R.string.logout) { logout() },
            MenuItem(Icons.Default.Cancel, R.string.exit) { logout() }
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