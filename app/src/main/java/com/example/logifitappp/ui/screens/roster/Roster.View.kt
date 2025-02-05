package com.example.logifitappp.ui.screens.roster

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.Chip
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.Loader
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.cards.InformationCard
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.RosterViewModel

@Composable
fun RosterView(
    appViewModel: AppViewModel,
    navigation: NavHostController
) {
    val scrollState = rememberLazyListState()

    val rosterViewModel = hiltViewModel<RosterViewModel, RosterViewModel.RosterViewModelFactory>{
        it.create(appViewModel.user)
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        rosterViewModel.checkForNewRoster()
        rosterViewModel.markAsRefresh()
    }

    ScrollablePage(
        topBar = {
            ColumnStackHeader(
                action = {
                    IconButton(
                        onClick = { navigation.navigate(MainRoutes.RosterRecording) }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                navigation = navigation,
                title = stringResource(id = R.string.roster_title)
            )
        },
        state = scrollState
    ) {
        rosterViewModel.state.roster?.data?.let {
            items(it) { roster ->
                InformationCard(
                    icon = Icons.Default.LocationOn,
                    label = roster.location.name,
                    suffixComponent = {
                        Chip(
                            label = roster.status,
                            status = if (roster.status == "ACTIVO") ChipStatusEnum.SUCCESS else ChipStatusEnum.DANGER
                        )
                    }
                ) {
                    Column(Modifier.padding(start = 22.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconText(
                                icon = Icons.Default.CalendarMonth,
                                iconColor = MaterialTheme.colorScheme.onSurface,
                                iconSize = 10.dp,
                                label = DateTimeUtils.parse(roster.startDate, "yyyy-MM-dd", "dd/MM/yyyy"),
                                labelTypography = MaterialTheme.typography.bodyMedium,
                            )

                            Text(
                                text = " - ",
                                typography = MaterialTheme.typography.bodyMedium
                            )

                            IconText(
                                icon = Icons.Default.CalendarMonth,
                                iconColor = MaterialTheme.colorScheme.onSurface,
                                iconSize = 10.dp,
                                label = DateTimeUtils.parse(roster.endDate, "yyyy-MM-dd", "dd/MM/yyyy"),
                                labelTypography = MaterialTheme.typography.bodyMedium,
                            )
                        }

                        roster.comment?.let { comment ->
                            IconText(
                                icon = Icons.Default.Edit,
                                iconColor = MaterialTheme.colorScheme.onSurface,
                                iconSize = 10.dp,
                                label = comment,
                                labelTypography = MaterialTheme.typography.bodyMedium,
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = DateTimeUtils.parse(roster.createdAt, "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy HH:mm:ss"),
                            typography = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))
            }
        }

        if (rosterViewModel.state.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Loader()
                }
            }
        }
    }

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.layoutInfo }
            .collect { info ->
                val visibleItems = info.visibleItemsInfo

                if (visibleItems.isEmpty()) return@collect

                val lastVisibleItemIndex = visibleItems.last().index
                val totalItems = info.totalItemsCount

                if (lastVisibleItemIndex != totalItems - 1 || rosterViewModel.state.isLoading) return@collect

                rosterViewModel.fetchRosterPage()
            }
    }
}