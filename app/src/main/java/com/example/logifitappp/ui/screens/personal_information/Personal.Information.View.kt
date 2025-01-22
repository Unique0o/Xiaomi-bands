package com.example.logifitappp.ui.screens.personal_information

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.components.personalInformation.PersonalInfoItem
import com.example.logifitappp.ui.components.personalInformation.ProfilePhoto
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.PersonalInfo.PersonalInfoUiState
import com.example.logifitappp.viewmodel.views.PersonalInformationViewModel


@Composable
fun PersonalInformationView(
    navigation: NavHostController
) {
    val viewModel: PersonalInformationViewModel = hiltViewModel()
    val personalInfoState by viewModel.personalInfo.collectAsState()
    val appViewModel: AppViewModel = hiltViewModel()
    val user = appViewModel.user

    SimplePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.personal_information_title)
            )
        }
    ) {
        when (personalInfoState) {
            is PersonalInfoUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }

            is PersonalInfoUiState.Success -> {
                val info = (personalInfoState as PersonalInfoUiState.Success).data
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    item {
                        ProfilePhoto(
                            user?.profilePhoto.toString(),
                            onPhotoClick = { viewModel.onPhotoClick() })
                    }
                    items(info.size) { index ->
                        val item = info[index]
                        PersonalInfoItem(
                            label = item.label,
                            value = item.value,
                            isValueSelected = item.isValueSelected,
                            onClick = { }
                        )
                    }
                }
            }

            is PersonalInfoUiState.Error -> {
                Text(
                    text = (personalInfoState as PersonalInfoUiState.Error).message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}