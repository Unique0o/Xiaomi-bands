package com.example.logifitappp.ui.components.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun ScrollablePage(
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    bottomBar: @Composable () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(16.dp),
    state: LazyListState = rememberLazyListState(),
    topBar: @Composable () -> Unit = {},
    content: LazyListScope.() -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = bottomBar,
        topBar = topBar
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                bottom = innerPadding.calculateBottomPadding() + contentPadding.calculateBottomPadding(),
                end = innerPadding.calculateEndPadding(LayoutDirection.Ltr) + contentPadding.calculateEndPadding(LayoutDirection.Ltr),
                start = innerPadding.calculateStartPadding(LayoutDirection.Ltr) + contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                top = innerPadding.calculateTopPadding() + contentPadding.calculateTopPadding()
            ),
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            state = state
        ) {
            content()
        }
    }
}