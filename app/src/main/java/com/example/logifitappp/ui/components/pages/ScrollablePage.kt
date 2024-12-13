package com.example.logifitappp.ui.components.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.ui.unit.dp
import com.example.logifitappp.core.utils.plus

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
            contentPadding = innerPadding + contentPadding,
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            state = state
        ) {
            content()
        }
    }
}