package com.example.logifitappp.ui.screens.graphics

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.graphics.InfoBarColorGraph
import com.example.logifitappp.ui.components.graphics.MeasurementHeartCard
import com.example.logifitappp.data.models.HeartRateData
import com.example.logifitappp.ui.components.graphics.StepHomeCard
import com.example.logifitappp.ui.components.headers.UserProfileCard
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.ui.components.titles.IconTitle

@Composable
fun Graphics() {
    SimplePage(
        content = {
            UserProfileCard(
                userName = "MARIA MERCEDES",
                userType = "PREMIUM",
                profileImageRes = R.drawable.user1,
                plan = "PREMIUM",
                onNotificationClick = { }
            )
            IconTitle(
                icon = ImageVector.vectorResource(id = R.drawable.ic_weather_night),
                text = stringResource(id = R.string.my_sleep)
            )
            val data = List(17) { (1..100).random() }
            InfoBarColorGraph(
                title = stringResource(id = R.string.information_between),
                timeRange = "19:00 - 07:00",
                data = data,
                indicatorInformation = "7h 36min",
                icon = painterResource(id = R.drawable.ic_refresh)
            )
            IconTitle(
                icon = ImageVector.vectorResource(id = R.drawable.ic_shoe_sneaker),
                text = stringResource(id = R.string.my_steps)
            )
            val steps = listOf(10, 20, 15, 30, 25, 35, 40)
            StepHomeCard(
                steps= steps,
                title= "200",
                indicatorInformation= "1200 pasos",
                icon = painterResource(id = R.drawable.ic_fire)
            )
            IconTitle(
                icon = ImageVector.vectorResource(id = R.drawable.ic_heart_pulse),
                text = stringResource(id = R.string.my_heart_rate)
            )
            val heartdata = HeartRateData(
                date = "Noviembre 20, 2023",
                minRate = 70,
                maxRate = 101,
                timeRange = "02:00 - 02:30",
                ranges = listOf(
                    10 to 35,
                    25 to 50,
                    15 to 40,
                    20 to 40,
                    10 to 40,
                )
            )
            MeasurementHeartCard(
                heartRateData = heartdata,
                indicatorInformation = "90 LPM",
                icon = painterResource(id = R.drawable.ic_heart_cog)
            )

        }
    )
}



@Preview(showBackground = true)
@Composable
fun GraphicsPreview() {
    LogifitApppTheme {
        Graphics()
    }
}

@Preview(showBackground = true)
@Composable
fun GraphicsDarkModePreview() {
    LogifitApppTheme(darkTheme = true) {
        Graphics()
    }
}