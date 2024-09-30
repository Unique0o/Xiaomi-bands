package com.example.logifitappp.ui.screens.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.graphics.CardLayout
import com.example.logifitappp.ui.components.headers.CardHeader
import com.example.logifitappp.ui.components.home.CardItemButton
import com.example.logifitappp.ui.components.home.ConnectedIndicator
import com.example.logifitappp.ui.components.menu.MenuItem
import com.example.logifitappp.ui.components.menu.SideMenu
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun HomeScreen(
    navigation: NavHostController
) {
    var isSideMenuOpen by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {

        CardHeader(
            userName = "MARIA MERCEDES",
            userType = "PREMIUM",
            profileImageRes = R.drawable.user1,
            onNotificationClick = { },
            onProfileImageClick = { isSideMenuOpen = true },
            plan = "PREMIUM",
            bodyComponent = {
                CardLayout(bodyComponent = { /*TODO*/ },
                    icon = painterResource(id = R.drawable.ic_clock),
                    iconSize = 20.dp,
                    label = stringResource(id = R.string.schedule),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,

                    suffixComponent = {
                        ConnectedIndicator(
                            text = stringResource(id = R.string.status),
                            color = Green298,
                            backgroundColor = Lime70,
                            pointColor = Green298
                        )
                    }
                )
                CardLayout(bodyComponent = { /*TODO*/ },
                    icon = painterResource(id = R.drawable.ic_location),
                    iconSize = 24.dp,
                    label = stringResource(id = R.string.my_location),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    suffixComponent = {
                        ConnectedIndicator(
                            text = stringResource(id = R.string.PGT),
                            color = Green298,
                            backgroundColor = Lime70,
                            pointColor = Green298
                        )
                    }
                )
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
        SimplePage(
            content = {
                CardItemButton(
                    title = stringResource(id = R.string.my_device),
                    description = stringResource(id = R.string.card_description),
                    icon = R.drawable.ic_watch,
                    iconButton = R.drawable.ic_add,
                    buttonColor = Blue690,
                    onClick = { /*TODO*/ }
                )
                CardItemButton(
                    title = stringResource(id = R.string.title_my_test),
                    description = stringResource(id = R.string.card_description_message),
                    icon = R.drawable.ic_test,
                    iconButton = R.drawable.ic_add,
                    buttonColor = Blue690,
                    onClick = { /*TODO*/ }
                )
            }

        )
    }
    // SideMenu
    SideMenu(
        isOpen = isSideMenuOpen,
        onClose = { isSideMenuOpen = false },
        name = "MARIA MERCEDES",
        role = "Operador en LOGIFIT",
        avatarResId = R.drawable.user1,
        menuItems = listOf(
            MenuItem(Icons.Default.Person, "Información personal") { /* TODO */ },
            MenuItem(Icons.Default.Work, "Información laboral") { /* TODO */ },
            MenuItem(Icons.Default.Favorite, "Información de salud") { /* TODO */ },
            MenuItem(Icons.Default.Help, "Ayuda") { /* TODO */ },
            MenuItem(Icons.Default.Description, "Términos y condiciones") { /* TODO */ },
            MenuItem(Icons.Default.ExitToApp, "Cerrar sesión") { /* TODO */ }
        )
    )
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    LogifitApppTheme {
        HomeScreen(rememberNavController())

    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenDarkModePreview() {
    LogifitApppTheme(darkTheme = true) {
        HomeScreen(rememberNavController())

    }
}