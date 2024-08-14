package com.example.logifitappp.ui.screens.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.logifitappp.ui.components.graphics.EmptyCardHour
import com.example.logifitappp.ui.components.headers.BackHeader
import com.example.logifitappp.ui.theme.Orange170
import com.example.logifitappp.ui.theme.Rose120

@Composable
fun HeartRateDetailEmpty() {
    val stepData = List(24) { (1..50).random() }

    Scaffold(
        topBar = {
           BackHeader(
                onBackClick = {  },
                subtitle = "Mi ritmo cardíaco",
                modifier = Modifier.background(Color.White)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EmptyCardHour(
                    steps = stepData,
                    maxValue = 50,
                    barColor = Orange170,
                    accentColor = Rose120,
                    title = "Ritmo Cardíaco",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeartRateDetailEmptyPreview() {
    HeartRateDetailEmpty()
}
