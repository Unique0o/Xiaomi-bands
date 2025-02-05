package com.example.logifitappp.ui.components.headers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.LicenseText
import com.example.logifitappp.ui.components.ProgressiveImage
import com.example.logifitappp.ui.components.Pulse
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.viewmodel.components.BottomTabsHeaderViewModel
import com.example.logifitappp.viewmodel.AppViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomTabsHeader(
    appViewModel: AppViewModel,
    drawerState: DrawerState,
    navigation: NavHostController,
    content: @Composable () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val user = appViewModel.user

    val bottomTabsHeaderViewModel: BottomTabsHeaderViewModel = viewModel()

    DisposableEffect(Unit) {
        val receiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                when (intent.action) {
                    App.NOTIFICATION -> bottomTabsHeaderViewModel.checkForNewNotification()
                }
            }
        }

        val filterLocal = IntentFilter()
        filterLocal.addAction(App.NOTIFICATION)
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filterLocal)

        onDispose {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        bottomTabsHeaderViewModel.checkForNewNotification()
    }

    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(bottomStart =  24.dp, bottomEnd = 24.dp)
    ) {
        TopAppBar(
            actions = {
                IconButton(
                    onClick = { navigation.navigate(MainRoutes.Notifications) }
                ) {
                    Box(contentAlignment = Alignment.BottomStart) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )

                        if (bottomTabsHeaderViewModel.hasNewNotification) {
                            Pulse()
                        }
                    }
                }
            },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
            navigationIcon = {
                ProgressiveImage(
                    default = R.drawable.ic_default_profile_photo,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .clickable {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                    url = user?.profilePhoto
                )
            },
            title = {
                Column {
                    Text(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        text = stringResource(id = R.string.greeting),
                        typography = MaterialTheme.typography.labelMedium
                    )

                    Text(
                        text = user?.firstName ?: "",
                        typography = MaterialTheme.typography.displayLarge
                    )

                    LicenseText(license = user?.license)
                }
            }
        )

        content()
    }
}