package com.example.logifitappp.ui.screens.graphics


import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.graphics.InfoBarFocusGraph
import com.example.logifitappp.ui.components.graphics.SleepInfoCard
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.home.CardItem
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.components.titles.IconTitle
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.ui.theme.Orange170
import com.example.logifitappp.ui.theme.Rose120

@Composable
fun SleepDetailFocusScreen(
    navigation: NavHostController
) {
    SimplePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.my_sleep)
            )
        },
        content = {
            InfoBarFocusGraph(
                data = List(17) { (1..100).random() },
                time = "7h 36min",
                hour = "19:00 - 07:00",
                specialBarIndex = 0
            )
            IconTitle(
                icon = ImageVector.vectorResource(id = R.drawable.ic_chart_box_outline),
                text = stringResource(id = R.string.summary)
            )
            SleepInfoCard()
            CardItem(
                title = stringResource(id = R.string.sleep_condition),
                status = stringResource(id = R.string.status_person),
                R.drawable.ic_weather_night,
                statusColor = Green298,
                backgroundColor = Lime70,
                modifier = Modifier.padding()
            )
            CardItem(
                title = stringResource(id = R.string.title_fatige),
                status = stringResource(id = R.string.status_no_apto),
                R.drawable.ic_snore_sleep,
                statusColor = Rose120,
                backgroundColor = Orange170,
                modifier = Modifier.padding()
            )
            CardItem(
                title = stringResource(id = R.string.average_heart_rate),
                status = " 70 LPM",
                R.drawable.ic_heart_pulse,
                statusColor = Green298,
                backgroundColor = Lime70,
                modifier = Modifier.padding()
            )
        }
    )
}


@Preview(showBackground = true)
@Composable
fun SleepDetailFocusPreview() {
    LogifitApppTheme {
        SleepDetailFocusScreen(navigation = NavHostController(LocalContext.current))
    }
}

@Preview(showBackground = true)
@Composable
fun SleepDetailFocusDarkPreview() {
    LogifitApppTheme(darkTheme = true) {
        SleepDetailFocusScreen(navigation = NavHostController(LocalContext.current))
    }
}