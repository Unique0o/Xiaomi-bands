package com.example.logifitappp.ui.screens.graphics


import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.graphics.EmptyCardHour
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun StepsDetailEmptyScreen(navigation: NavHostController) {
    val stepData = List(24) { (1..50).random() }

    SimplePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.my_steps)
            )
        },
        content = {
            EmptyCardHour(
                steps = stepData,
                maxValue = 50,
                barColor = Lime70,
                accentColor =Green298,
                title = stringResource(id = R.string.steps_taken),
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun StepsDetailEmptyPreview() {
    LogifitApppTheme{ StepsDetailEmptyScreen(rememberNavController()) }
}
@Preview
@Composable
fun StepsDetailEmptyDarkModePreview() {
    LogifitApppTheme(darkTheme = true){ StepsDetailEmptyScreen(rememberNavController()) }
}
