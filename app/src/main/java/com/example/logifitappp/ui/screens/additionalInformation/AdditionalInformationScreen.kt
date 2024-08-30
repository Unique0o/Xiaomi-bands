package com.example.logifitappp.ui.screens.additionalInformation


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.viewmodel.views.AdditionalInformation.AdditionalInformationViewModel

@Composable
fun AdditionalInformationScreen(
    navigation: NavHostController
) {
    val additionalInformationViewModel: AdditionalInformationViewModel = viewModel()

    SimplePage(
        content = {

            AdditionalInformationForm(
                additionalInformationViewModel = additionalInformationViewModel,
                selectableBottomSheetViewModel = viewModel(),
                onSubmit = {
                    navigation.navigate(MainRoutes.AdditionalInformationPicture)
                }
            )

        }
    )
}


@Composable
@Preview
fun AdditionalInformationScreenPreview() {
    LogifitApppTheme {
        AdditionalInformationScreen(rememberNavController())
    }
}

@Composable
@Preview
fun AdditionalInformationScreenDarkPreview() {
    LogifitApppTheme(darkTheme = true) {
        AdditionalInformationScreen(rememberNavController())
    }
}