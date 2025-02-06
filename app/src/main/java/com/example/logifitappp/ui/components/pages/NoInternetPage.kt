package com.example.logifitappp.ui.components.pages

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R

@Composable
fun NoInternetPage(
    contentPadding: PaddingValues = PaddingValues(10.dp),
    topBar: @Composable () -> Unit = {},
    action: () -> Unit
) {
    IconMessagePage(
        action = action,
        buttonIcon = Icons.Default.Refresh,
        buttonLabel = stringResource(id = R.string.button_try_again),
        contentPadding = contentPadding,
        message = stringResource(R.string.no_internet_connection_page_message),
        pageIcon = Icons.Default.WifiOff,
        topBar = topBar
    )
}