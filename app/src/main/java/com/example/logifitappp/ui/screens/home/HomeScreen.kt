package com.example.logifitappp.ui.screens.home


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.cards.InformationOptionCard
import com.example.logifitappp.ui.components.headers.BottomTabsHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.viewmodel.views.HomeViewModel

@Composable
fun HomeScreen(
    navigation: NavHostController
) {
    //var isSideMenuOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val homeViewModel: HomeViewModel = hiltViewModel()

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        val receiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                when (intent.action) {
                    App.ACTION_NEW_DATA -> {
                        val wearable = intent.getParcelableExtra<Wearable>(Wearable.EXTRA_DEVICE)!!
                        homeViewModel.refreshSingleWearable(wearable)
                    }
                }
            }
        }

        val filterLocal = IntentFilter()
        filterLocal.addAction(App.ACTION_NEW_DATA)
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filterLocal)

        homeViewModel.refreshPairedWearables()
    }

    SimplePage(
        content = {
            if (homeViewModel.wearables.size == 0) {
                InformationOptionCard(
                    buttonIcon = Icons.Filled.Add,
                    icon = Icons.Outlined.Watch,
                    modifier = Modifier.padding(horizontal = 6.dp),
                    onClick = { navigation.navigate(MainRoutes.WearableDetection) },
                    paragraph = stringResource(id = R.string.reminder_message),
                    title =  stringResource(id = R.string.my_device)
                )
            } else {
                HomeWearable(
                    connect = { homeViewModel.connect(it) },
                    fetchActivities = { homeViewModel.fetchActivities(it) },
                    sleeps = homeViewModel.sleeps,
                    wearable = homeViewModel.wearables[0]
                )
            }
        },
        topBar = {
            BottomTabsHeader(
                children = {
                    HomeShiftCard()
                    HomeLocationCard()
                },
                navigation = navigation
            )
        }
    )

    /*Column(
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
        menuItems = listOf(
            MenuItem(Icons.Default.Person, "Información personal") { /* TODO */ },
            MenuItem(Icons.Default.Work, "Información laboral") { /* TODO */ },
            MenuItem(Icons.Default.Favorite, "Información de salud") { /* TODO */ },
            MenuItem(Icons.Default.Help, "Ayuda") { /* TODO */ },
            MenuItem(Icons.Default.Description, "Términos y condiciones") { /* TODO */ },
            MenuItem(Icons.Default.ExitToApp, "Cerrar sesión") { /* TODO */ }
        )
    )*/
}