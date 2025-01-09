package com.example.logifitappp.ui.screens.drowsiness_tests

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.LoaderPage
import com.example.logifitappp.ui.components.pages.NoInternetPage
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.views.DrowsinessTestViewModel

@Composable
fun DrowsinessTestsView(
    navigation: NavHostController
) {
    val drowsinessTestViewModel: DrowsinessTestViewModel = hiltViewModel()

    if (drowsinessTestViewModel.state.isLoading) {
        LoaderPage()
        return
    }

    if (drowsinessTestViewModel.state.hasFetchTestsFailed) {
        NoInternetPage { drowsinessTestViewModel.fetchTests() }
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
        items(drowsinessTestViewModel.tests) {
            Row(
                Modifier
                    .clickable { navigation.navigate(MainRoutes.DrowsinessTestDetail(it.id)) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    contentDescription = null,
                    imageVector = Icons.AutoMirrored.Filled.InsertDriveFile,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.width(16.dp))

                Text(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                    text = it.name,
                    typography = MaterialTheme.typography.headlineLarge
                )

                Spacer(Modifier.width(16.dp))

                Icon(
                    contentDescription = null,
                    imageVector = Icons.Outlined.ChevronRight,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}