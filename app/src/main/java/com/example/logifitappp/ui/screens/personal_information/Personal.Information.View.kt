package com.example.logifitappp.ui.screens.personal_information

import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.modals.MessageModal
import com.example.logifitappp.ui.components.pages.NoInternetPage
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.PersonalInformationViewModel


@Composable
fun PersonalInformationView(
    appViewModel: AppViewModel,
    navigation: NavHostController
) {

    val personalInformationViewModel =
        hiltViewModel<PersonalInformationViewModel, PersonalInformationViewModel.PersonalInformationViewModelFactory> {
            it.create(appViewModel.user)
        }


    MessageModal(
        onClose = { personalInformationViewModel.stopProcessing() },
        onDismissRequest = { personalInformationViewModel.stopProcessing() },
        status = personalInformationViewModel.state.storingInformationStatus,
        visible = personalInformationViewModel.state.isNecessaryDataFetching || personalInformationViewModel.state.isInformationStoring
    )


    if (personalInformationViewModel.state.hasNecessaryDataFetchingFailed) {
        NoInternetPage { personalInformationViewModel.fetchNecessaryData() }
        return
    }

    SimplePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.personal_information_title)
            )
        }
    ) {
        PersonalInfoScreen(appViewModel, personalInformationViewModel)
    }
}
