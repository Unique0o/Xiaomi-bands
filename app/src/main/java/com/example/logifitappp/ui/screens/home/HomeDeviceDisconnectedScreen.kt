package com.example.logifitappp.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.home.HeaderHome
import com.example.logifitappp.ui.components.home.OptionsList
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun HomeDeviceDisconnectedScreen() {
    SimplePage(
        content = {
            //BackgroundCurve(modifier = Modifier.fillMaxSize())
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                HeaderHome(
                    title = "Bienvenido de vuelta",
                    nameUser = "MARIA MERCEDEZ",
                    plan = stringResource(id = R.string.plan)
                )
                OptionsList()

            }
        })
}

@Preview(showBackground = true)
@Composable
fun HomeDeviceDisconnectedScreenPreview() {
    LogifitApppTheme {
        HomeDeviceDisconnectedScreen()

    }
}

@Preview(showBackground = true)
@Composable
fun HomeDeviceDisconnectedScreenDarkModePreview() {
    LogifitApppTheme(darkTheme = true) {
        HomeDeviceDisconnectedScreen()

    }
}