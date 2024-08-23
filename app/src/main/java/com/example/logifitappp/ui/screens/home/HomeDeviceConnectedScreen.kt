package com.example.logifitappp.ui.screens.home


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.home.OptionsList
import com.example.logifitappp.ui.components.home.HeaderHome
import com.example.logifitappp.ui.components.home.CardTest
import com.example.logifitappp.ui.components.home.TestsSection
import com.example.logifitappp.ui.components.home.TitleSmartBandHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun HomeDeviceConnectedScreen() {
    SimplePage(
        content = {
            //BackgroundCurve(modifier = Modifier.fillMaxSize())

            HeaderHome(
                title = "Bienvenido de vuelta",
                nameUser = "MARIA MERCEDEZ",
                plan = stringResource(id = R.string.plan)
            )
            OptionsList()
            Spacer(modifier = Modifier.height(16.dp))
            TitleSmartBandHeader()
            CardTest()
            TestsSection(buttonColor = Blue690)


        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeDeviceConnectedScreenPreview() {
    LogifitApppTheme {
        HomeDeviceConnectedScreen()

    }
}

@Preview(showBackground = true)
@Composable
fun HomeDeviceConnectedScreenDarkModePreview() {
    LogifitApppTheme(darkTheme = true) {
        HomeDeviceConnectedScreen()

    }
}