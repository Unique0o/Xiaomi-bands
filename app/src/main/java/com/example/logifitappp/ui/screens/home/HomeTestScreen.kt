package com.example.logifitappp.ui.screens.home


import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.graphics.CardLayout
import com.example.logifitappp.ui.components.home.OptionsList
import com.example.logifitappp.ui.components.home.HeaderHome
import com.example.logifitappp.ui.components.home.CardTest
import com.example.logifitappp.ui.components.home.ConnectedIndicator
import com.example.logifitappp.ui.components.home.TestFatigaItem
import com.example.logifitappp.ui.components.home.TestsSection
import com.example.logifitappp.ui.components.home.TitleSmartBandHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun HomeTestScreen() {
    SimplePage(
        content = {
            //BackgroundCurve(modifier = Modifier.fillMaxSize())

            HeaderHome(
                title = "Bienvenido de vuelta",
                nameUser = "MARIA MERCEDEZ",
                plan = stringResource(id = R.string.plan)
            )
            CardLayout(bodyComponent = { /*TODO*/ },
                icon = painterResource(id = R.drawable.ic_clock),
                iconSize = 24.dp,
                label = stringResource(id = R.string.schedule),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,

                suffixComponent = {
                    ConnectedIndicator(
                        text = stringResource(id = R.string.status),
                        color = Green298,
                        backgroundColor = Lime70,
                        pointColor = Green298
                    )
                }
            )
            CardLayout(bodyComponent = { /*TODO*/ },
                icon = painterResource(id = R.drawable.ic_location),
                iconSize = 24.dp,
                label = stringResource(id = R.string.my_location),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                suffixComponent = {
                    ConnectedIndicator(
                        text = stringResource(id = R.string.PGT),
                        color = Green298,
                        backgroundColor = Lime70,
                        pointColor = Green298
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TitleSmartBandHeader()
            CardTest()
            TestsSection(buttonColor = Green298)
            TestFatigaItem()

        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeTestScreenPreview() {
    LogifitApppTheme {
        HomeTestScreen()

    }
}

@Preview(showBackground = true)
@Composable
fun HomeTestDarkModeScreenPreview() {
    LogifitApppTheme(darkTheme = true) {
        HomeTestScreen()

    }
}