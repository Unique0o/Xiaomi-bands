package com.example.logifitappp.ui.components.pages

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.logifitappp.R

@Composable
fun NoInternetPage(
    topBar: @Composable () -> Unit = {},
    action: () -> Unit
) {
    IconMessagePage(
        action = action,
        buttonIcon = Icons.Default.Refresh,
        buttonLabel = stringResource(id = R.string.button_try_again),
        message = stringResource(R.string.no_internet_connection_page_message),
        pageIcon = Icons.Default.WifiOff,
        topBar = topBar
    )
}