package com.example.logifitappp.ui.screens.app_language

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.data.models.Language
import com.example.logifitappp.ui.components.appLanguage.LanguageOption
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.views.appLanguage.LanguageChangeHelper

@Composable
fun AppLanguageView(
    navigation: NavHostController,
    languageChangeHelper: LanguageChangeHelper = LanguageChangeHelper()
) {
    val context = LocalContext.current
    val currentLanguageCode: String = languageChangeHelper.getLanguageCode(context)
    val currentLanguage by remember { mutableStateOf(currentLanguageCode) }

    val allLanguages = listOf(
        Language("default", R.string.by_default),
        Language("en", R.string.english),
        Language("es", R.string.spanish)
    )

    ScrollablePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.app_language)
            )
        }
    ) {
        items(allLanguages) { language ->
            LanguageOption(
                labelResId = language.name,
                isSelected = language.code == currentLanguage,
                onClick = { languageChangeHelper.changeLanguage(context, language.code) }
            )
        }
    }
}
