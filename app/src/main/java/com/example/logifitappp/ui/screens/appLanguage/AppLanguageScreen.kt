package com.example.logifitappp.ui.screens.appLanguage

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.appLanguage.LanguageOption
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.viewmodel.views.appLanguage.AppLanguageViewModel


@Composable
fun AppLanguageScreen(
    navigation: NavHostController,
) {
    val viewModel: AppLanguageViewModel = hiltViewModel()
    val currentLanguage by viewModel.currentLanguage.collectAsState()

    SimplePage(
        content = {
            Column {
                LanguageOption(R.string.by_default, currentLanguage == "default") {
                    viewModel.setLanguage("default")
                }
                LanguageOption(R.string.english, currentLanguage == "en") {
                    viewModel.setLanguage("en")
                }
                LanguageOption(R.string.spanish, currentLanguage == "es") {
                    viewModel.setLanguage("es")
                }
            }
        },
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.language)
            )
        }
    )
}

@Preview
@Composable
fun AppLanguageScreenPreview() {
    MaterialTheme {
        LogifitApppTheme {
            AppLanguageScreen(rememberNavController())
        }
    }
}