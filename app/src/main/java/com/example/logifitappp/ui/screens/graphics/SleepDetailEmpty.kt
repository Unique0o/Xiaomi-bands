package com.example.logifitappp.ui.screens.graphics


import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.graphics.EmptyInfoGraphDetail
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme


@Composable
fun SleepDetailEmpty(navigation: NavHostController) {
    SimplePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.my_sleep)
            )
        },
        content = {
            EmptyInfoGraphDetail(
                title = stringResource(id = R.string.sleep_time),
                titleGraph = stringResource(id = R.string.information_between),
                timeRange = "19:00 - 07:00"
            )
        }
    )
}

@Preview
@Composable
fun SleepDetailEmptyPreview() {
    LogifitApppTheme {
        SleepDetailEmpty(rememberNavController())
    }
}
@Preview
@Composable
fun SleepDetailEmptyDarkModePreview() {
    LogifitApppTheme(darkTheme = true) {
        SleepDetailEmpty(rememberNavController())
    }
}