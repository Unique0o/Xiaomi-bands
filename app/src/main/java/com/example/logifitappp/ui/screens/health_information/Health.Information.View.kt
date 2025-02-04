package com.example.logifitappp.ui.screens.health_information

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.HealthInfo.HealthInfoCard
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.components.forms.SelectableBottomSheetOnly
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.utils.Constants.bloodTypeTitles
import com.example.logifitappp.utils.Constants.genderTitles
import com.example.logifitappp.utils.Constants.heightTitles
import com.example.logifitappp.utils.Constants.weightTitles
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.HealthInfo.HealthInfoUiState
import com.example.logifitappp.viewmodel.views.HealthInformationViewModel

@Composable
fun HealthInformationView(
    appViewModel: AppViewModel,
    navigation: NavHostController
) {

    val viewModel =
        hiltViewModel<HealthInformationViewModel, HealthInformationViewModel.HealthInformationViewModelFactory> {
            it.create(appViewModel.user)
        }

    val health by viewModel.healthInfo.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedSection by remember { mutableStateOf<String?>(null) }


    if (showBottomSheet && selectedSection != null) {
        when (selectedSection) {
            "Blood Type" -> DocumentTypeSection(true, viewModel) { showBottomSheet = false }
            "Gender" -> DocumentTypeSection(false, viewModel) { showBottomSheet = false }
        }
    }

    SimplePage(content = {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            when (health) {
                HealthInfoUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                }

                is HealthInfoUiState.Success -> {
                    val info = (health as HealthInfoUiState.Success).data
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f) // 🔹 Takes only available space, leaving room for the button
                    ) {
                        items(info.size) { index ->
                            val item = info[index]
                            HealthInfoCard(
                                title = item.title,
                                initialValue = item.value,
                                unit = item.unit,
                                imageRes = item.imageRes,
                                onEditClick = {
                                    selectedSection = when (item.title) {
                                        in weightTitles -> "Green298"
                                        in heightTitles -> "Orange390"
                                        in bloodTypeTitles -> "Blood Type"
                                        in genderTitles -> "Gender"
                                        else -> null
                                    }
                                    showBottomSheet = true
                                },
                                onSaveClick = { newValue -> println("New Value: $newValue") }
                            )
                        }
                    }
                }

                is HealthInfoUiState.Error -> {
                    Text(
                        text = (health as HealthInfoUiState.Error).message,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp)) // Adds spacing before button

            Button(
                onClick = {
                    // Handle button click
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = stringResource(id = R.string.save),
            )
        }
    },
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.health_info)
            )
        }
    )
}

@Composable
fun DocumentTypeSection(
    isBlood: Boolean,
    healthInformationViewModel: HealthInformationViewModel,
    onDismiss: () -> Unit
) {
    SelectableBottomSheetOnly(
        elements = if (isBlood) healthInformationViewModel.getBloodList() else healthInformationViewModel.getGenderList(),
        onChange = { selectedItem ->
            healthInformationViewModel.updateSelectedValue(
                isBlood,
                selectedItem
            )
            onDismiss()
        },
        title = if (isBlood) {
            stringResource(R.string.blood_type_title)
        } else {
            stringResource(R.string.gender_type_title)
        },
        value = if (isBlood) healthInformationViewModel.getBloodList()
            .find { it.id == healthInformationViewModel.state.bloodType }
        else healthInformationViewModel.getGenderList()
            .find { it.id == healthInformationViewModel.state.gender }
    )
}

