package com.example.logifitappp.enums

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diversity1
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.DrawerState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.navigation.routes.MainRoutes

enum class SideBarScreenEnum(
    val action: suspend (drawerState: DrawerState, navigation: NavHostController, callback: (() -> Unit)?) -> Unit,
    @StringRes val label: Int,
    val icon: ImageVector
) {
    APP_LANGUAGE({ drawerState, navigation, _ ->
        navigation.navigate(MainRoutes.AppLanguage)
        drawerState.close()
    }, R.string.app_language, Icons.Default.Translate),

    EXIT({ drawerState, _, callback ->
        callback?.let { it() }
        drawerState.close()
    }, R.string.exit, Icons.Default.Close),

    HEALTH_INFORMATION({ drawerState, navigation, _ ->
        navigation.navigate(MainRoutes.HealthInformation)
        drawerState.close()
    }, R.string.health_information_title, Icons.Default.Diversity1),

    HELP({ drawerState, navigation, _ ->
        navigation.navigate(MainRoutes.Help)
        drawerState.close()
    }, R.string.help, Icons.Default.QuestionMark),

    LOGOUT({ drawerState, _, callback ->
        callback?.let { it() }
        drawerState.close()
    }, R.string.logout, Icons.AutoMirrored.Default.Logout),

    OCCUPATIONAL_INFORMATION({ drawerState, navigation, _ ->
        navigation.navigate(MainRoutes.OccupationalInformation)
        drawerState.close()
    }, R.string.occupational_information_title, Icons.Default.Badge),

    PERSONAL_INFORMATION({ drawerState, navigation, _ ->
        navigation.navigate(MainRoutes.PersonalInformation)
        drawerState.close()
    }, R.string.personal_information_title, Icons.Default.Person),

    ROSTER({ drawerState, navigation, _ ->
        navigation.navigate(MainRoutes.Roster)
        drawerState.close()
    }, R.string.roster_title, Icons.Default.CalendarMonth),

    TERMS_AND_CONDITIONS({ drawerState, navigation, _ ->
        navigation.navigate(MainRoutes.TermsAndConditions)
        drawerState.close()
    }, R.string.terms_and_conditions, Icons.Default.Shield),

    TRAININGS({ drawerState, navigation, _ ->
        navigation.navigate(MainRoutes.Trainings)
        drawerState.close()
    }, R.string.trainings_title, Icons.Default.School),

    WEARABLE_KEY_UPDATE({ drawerState, navigation, _ ->
        navigation.navigate(MainRoutes.WearableKeyUpdate)
        drawerState.close()
    }, R.string.wearable_key_update_title, Icons.Default.Key);
}