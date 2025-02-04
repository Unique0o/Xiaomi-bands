package com.example.logifitappp.ui.components

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.utils.AndroidUtils
import com.example.logifitappp.enums.SideBarScreenEnum
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.components.SideBarViewModel
import kotlinx.coroutines.launch

@Composable
fun SideBarContent(
    appViewModel: AppViewModel,
    drawerState: DrawerState,
    navigation: NavHostController
) {
    val user = appViewModel.user
    val activity = LocalContext.current as? Activity
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val scope = rememberCoroutineScope()
    val scrollableState = rememberScrollState()

    val sideBarViewModel = hiltViewModel<SideBarViewModel, SideBarViewModel.SideBarViewModelFactory>{
        it.create(user)
    }

    Column(
        modifier = Modifier
            .width(screenWidth * 0.8f)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .verticalScroll(scrollableState)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(horizontal = 8.dp, vertical = 40.dp)
        ) {
            ProgressiveImage(
                default = R.drawable.ic_default_profile_photo,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                url = user?.profilePhoto
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = user?.getFullName() ?: "",
                typography = MaterialTheme.typography.titleLarge
            )

            Text(
                text = user?.getRole() ?: "",
                typography = MaterialTheme.typography.labelMedium
            )

            AndroidUtils.getAppVersion(App.context)?.let {
                Text(
                    text = "V$it",
                    typography = MaterialTheme.typography.labelMedium
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        sideBarViewModel.options.map {
            IconText(
                modifier = Modifier
                    .clickable {
                        scope.launch {
                            it.action(drawerState, navigation) {
                                when (it) {
                                    SideBarScreenEnum.LOGOUT -> {
                                        App.wearableService.disconnect()
                                        appViewModel.logout()
                                    }

                                    SideBarScreenEnum.EXIT -> activity?.finish()
                                    else -> {}
                                }
                            }
                        }
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 16.dp),
                icon = it.icon,
                iconColor = if (it == SideBarScreenEnum.EXIT) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outlineVariant,
                iconSize = 18.dp,
                label = stringResource(it.label),
                labelColor = if (it == SideBarScreenEnum.EXIT) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outlineVariant,
                labelTypography = MaterialTheme.typography.bodyMedium,
                spaceBetween = 32.dp
            )

            if (it == SideBarScreenEnum.LOGOUT) Spacer(Modifier.weight(1f))
        }
    }
}