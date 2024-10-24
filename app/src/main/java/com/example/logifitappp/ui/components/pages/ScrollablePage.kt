package com.example.logifitappp.ui.components.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ScrollablePage(
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    bottomBar: @Composable () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    content:  LazyListScope.() -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = bottomBar,
        topBar = topBar
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                bottom = innerPadding.calculateBottomPadding() + 16.dp,
                end = 16.dp,
                start = 16.dp,
                top = innerPadding.calculateTopPadding() + 16.dp
            ),
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            content()
        }
    }
}