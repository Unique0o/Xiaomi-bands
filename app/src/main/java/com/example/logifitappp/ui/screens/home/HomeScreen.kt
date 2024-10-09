package com.example.logifitappp.ui.screens.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.logifitappp.ui.components.menu.SideMenu
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.viewmodel.views.SideMenuViewModel.SideMenuViewModel
import com.example.logifitappp.viewmodel.views.home.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    sideMenuViewModel: SideMenuViewModel,
    navController: NavHostController
) {
    var isSideMenuOpen by remember { mutableStateOf(false) }
    val menuItems by sideMenuViewModel.menuItems.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {

        CardHeader(
            user = viewModel.mockUsers[0],
            onNotificationClick = { },
            onProfileImageClick = { isSideMenuOpen = true },
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
        menuItems  = menuItems
    )
}


//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    LogifitApppTheme {
//        HomeScreen(HomeViewModel(), SideMenuViewModel(), rememberNavController())
//
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun HomeScreenDarkModePreview() {
//    LogifitApppTheme(darkTheme = true) {
//        HomeScreen(HomeViewModel(), SideMenuViewModel(),rememberNavController())
//
//    }
//}