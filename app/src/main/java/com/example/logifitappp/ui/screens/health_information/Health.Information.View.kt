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
import com.example.logifitappp.ui.components.forms.SelectableBottomSheetOnly
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Orange390
import com.example.logifitappp.ui.theme.Rose120
import com.example.logifitappp.ui.theme.Violet500
import com.example.logifitappp.viewmodel.views.HealthInfo.HealthInfoUiState
import com.example.logifitappp.viewmodel.views.HealthInformationViewModel
import com.example.logifitappp.viewmodel.views.PersonalInformationViewModel

@Composable
fun HealthInformationView(
    navigation: NavHostController
) {
    val viewModel: HealthInformationViewModel = hiltViewModel()
    val health by viewModel.healthInfo.collectAsState()
    val weightTitles = listOf("Weight", "Peso") // English & Spanish
    val heightTitles = listOf("Height", "Altura")
    val bloodTypeTitles = listOf("Blood type", "Tipo de sangre")
    val genderTitles = listOf("Gender", "Genero")

    SimplePage(content = {
        when (health) {
            HealthInfoUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }

            is HealthInfoUiState.Success -> {
                val info = (health as HealthInfoUiState.Success).data
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(info.size) { index ->
                        val item = info[index]
                        HealthInfoCard(
                            title = item.title,
                            value = item.value,
                            unit = item.unit,
                            imageRes = item.imageRes,
                            onEditClick = {
                                when (item.title) {
                                    in weightTitles -> Green298
                                    in heightTitles -> Orange390
                                    in bloodTypeTitles -> Rose120
                                    in genderTitles -> Violet500
                                }
                            }
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
fun DocumentTypeSection(personalInformationViewModel: PersonalInformationViewModel) {
    SelectableBottomSheetOnly(
        elements = personalInformationViewModel.state.documentTypes,
        onChange = {
            personalInformationViewModel.updateDocumentType(it)
        },
        title = stringResource(R.string.document_type_title),
        value = personalInformationViewModel.state.selectedDocumentType
    )
}

