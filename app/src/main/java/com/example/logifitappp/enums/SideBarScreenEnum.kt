package com.example.logifitappp.enums

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Diversity1
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.DrawerState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.navigation.routes.MainRoutes

enum class SideBarScreenEnum(
    val action: suspend (drawerState: DrawerState, navigation: NavHostController) -> Unit,
    @StringRes val label: Int,
    val icon: ImageVector
) {
    HEALTH_INFORMATION({ drawerState, navigation ->
        navigation.navigate(MainRoutes.HealthInformation)
        drawerState.close()
    }, R.string.health_information_title, Icons.Default.Diversity1),

    OCCUPATIONAL_INFORMATION({ drawerState, navigation ->
        navigation.navigate(MainRoutes.OccupationalInformation)
        drawerState.close()
    }, R.string.occupational_information_title, Icons.Default.Badge),

    PERSONAL_INFORMATION({ drawerState, navigation ->
        navigation.navigate(MainRoutes.PersonalInformation)
        drawerState.close()
    }, R.string.personal_information_title, Icons.Default.Person),

    ROSTER({ drawerState, navigation ->
        navigation.navigate(MainRoutes.Roster)
        drawerState.close()
    }, R.string.roster_title, Icons.Default.CalendarMonth),

    TRAININGS({ drawerState, navigation ->
        navigation.navigate(MainRoutes.Trainings)
        drawerState.close()
    }, R.string.trainings_title, Icons.Default.School);
}