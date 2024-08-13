package com.example.logifitappp.ui.screens.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.logifitappp.ui.components.graphics.EmptyInfoGraphDetail
import com.example.logifitappp.ui.components.headers.BackHeader


@Composable
fun SleepDetailEmpty() {
    val stepData = List(24) { (1..50).random() }

    Scaffold(
        topBar = {
            BackHeader(
                onBackClick = {  },
                subtitle = "Mi sueño",
                modifier = Modifier.background(Color.White)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            EmptyInfoGraphDetail(
                title = "Tiempo de sueño",
                titleGraph = "Información entre",
                timeRange = "19:00 - 07:00"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SleepDetailEmptyPreview() {
    SleepDetailEmpty()
}