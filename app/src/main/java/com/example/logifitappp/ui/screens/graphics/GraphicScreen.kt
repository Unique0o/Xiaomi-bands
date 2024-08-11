package com.example.logifitappp.ui.screens.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.graphics.EmptyInfoGraph
import com.example.logifitappp.ui.components.graphics.StepGraphCard
import com.example.logifitappp.ui.components.headers.UserProfileCard
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime30
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.theme.Orange170
import com.example.logifitappp.ui.theme.Rose120

@Composable
fun MainScreen1() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            UserProfileCard(
                userName = "MARIA MERCEDES",
                userType = "PREMIUM",
                profileImageRes = R.drawable.user1,
                plan = "PREMIUM",
                onNotificationClick = { },

                )
            Text(
                text = "Mi Sueño",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 10.dp, start = 30.dp)
            )

            EmptyInfoGraph(
                title = "Información entre",
                timeRange = "19:00 - 07:00",
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Mi pasos",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 16.dp, start = 30.dp)
            )
            val stepData = List(24) { kotlin.random.Random.nextFloat() }
            StepGraphCard(
                title = stringResource(id = R.string.kcal),
                data = stepData,
                modifier = Modifier.padding(16.dp),
                iconResId = R.drawable.ic_fire,
                backgroundColorConnect = Lime30,
                barColor = Lime70,
                accentColor = Green298
            )

            Text(
                text = "Mi Ritmo Cardiaco",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 16.dp, start = 30.dp)
            )
            StepGraphCard(
                title = stringResource(id = R.string.graph_card_heart_rate),
                data = stepData,
                modifier = Modifier.padding(16.dp),
                iconResId = R.drawable.ic_heart_rate,
                barColor = Orange170,
                backgroundColorConnect = Lime30,
                accentColor = Rose120
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GraphicsPreview() {
    LogifitApppTheme {
        MainScreen1()
    }
}


