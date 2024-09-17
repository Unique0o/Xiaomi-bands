package com.example.logifitappp.ui.screens.graphics


import com.example.logifitappp.ui.components.graphics.SleepSessionCard
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.graphics.AlarmClockCard
import com.example.logifitappp.ui.components.graphics.CardLayout
import com.example.logifitappp.ui.components.graphics.InfoBarDetailGraph
import com.example.logifitappp.ui.components.graphics.SleepInfoCard
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.home.ConnectedIndicator
import com.example.logifitappp.ui.components.titles.IconTitle
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.ui.theme.Orange170
import com.example.logifitappp.ui.theme.Rose120

@Composable
fun SleepDetailLargeScreen(navigation: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        ColumnStackHeader(
            navigation = navigation,
            title = stringResource(id = R.string.my_sleep)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            item {
                InfoBarDetailGraph(
                    data = List(17) { (1..100).random() },
                    time = "7h 36min",
                    hour = "19:00 - 07:00"
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                IconTitle(
                    icon = ImageVector.vectorResource(id = R.drawable.ic_chart_box_outline),
                    text = stringResource(id = R.string.summary)
                )
            }
            item {
                SleepInfoCard()
            }
            item {
                AlarmClockCard()
            }
            item {
                CardLayout(bodyComponent = { /*TODO*/ },
                    icon = painterResource(id = R.drawable.ic_weather_night),
                    iconSize = 20.dp,
                    label = stringResource(id = R.string.sleep_condition),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    style = Modifier.padding(2.dp),
                    suffixComponent = {
                        ConnectedIndicator(
                            text = stringResource(id = R.string.status_person),
                            color = Green298,
                            backgroundColor = Lime70,
                            pointColor = Green298,
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                )
            }
            item {
                CardLayout(bodyComponent = { /*TODO*/ },
                    icon = painterResource(id = R.drawable.ic_snore_sleep),
                    iconSize = 20.dp,
                    label = stringResource(id = R.string.title_fatige),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    style = Modifier.padding(2.dp),
                    suffixComponent = {
                        ConnectedIndicator(
                            text = stringResource(id = R.string.status_no_apto),
                            color = Rose120,
                            backgroundColor = Orange170,
                            pointColor = Rose120,
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                IconTitle(
                    icon = ImageVector.vectorResource(id = R.drawable.ic_lightbulb_off),
                    text = stringResource(id = R.string.naps),
                )
            }
            item {
                SleepSessionCard()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SleepDetailLargeScreenPreview() {
    LogifitApppTheme {
        SleepDetailLargeScreen(navigation = NavHostController(LocalContext.current))
    }
}

@Preview(showBackground = true)
@Composable
fun SleepDetailLargeScreenDarkModePreview() {
    LogifitApppTheme(darkTheme = true) {
        SleepDetailLargeScreen(navigation = NavHostController(LocalContext.current))
    }
}



