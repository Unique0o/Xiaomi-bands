package com.example.logifitappp.ui.screens.notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.LoaderPage
import com.example.logifitappp.ui.components.pages.NoInternetPage
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.views.NotificationsViewModel

@Composable
fun NotificationsView(
    navigation: NavHostController
) {
    val notificationsViewModel: NotificationsViewModel = hiltViewModel()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        notificationsViewModel.markAsRead()
    }

    if (notificationsViewModel.state.isLoading) {
        LoaderPage()
        return
    }

    if (notificationsViewModel.state.hasFetchNotificationsFailed) {
        NoInternetPage { notificationsViewModel.fetchNotifications() }
        return
    }

    ScrollablePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.notifications_title)
            )
        }
    ) {
        items(notificationsViewModel.notifications) {
            InformationCard(
                icon = Icons.Default.Description,
                label = it.data.title
            ) {
                Column(Modifier.padding(start = 22.dp)) {
                    Text(
                        text = it.data.body,
                        typography = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = DateTimeUtils.parse(it.createdAt, "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy HH:mm:ss"),
                        typography = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}