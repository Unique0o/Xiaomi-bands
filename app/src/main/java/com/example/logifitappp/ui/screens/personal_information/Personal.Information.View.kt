package com.example.logifitappp.ui.screens.personal_information


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.ui.components.BottomSheetSelectableImageSourceAuto
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.components.forms.SelectableBottomSheet
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.LoaderPage
import com.example.logifitappp.ui.components.pages.NoInternetPage
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.components.personalInformation.PersonalInfoItem
import com.example.logifitappp.ui.components.personalInformation.ProfilePhoto
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.PersonalInfo.PersonalInfoUiState
import com.example.logifitappp.viewmodel.views.PersonalInformationViewModel
import java.util.TimeZone


@Composable
fun PersonalInformationView(
    appViewModel: AppViewModel,
    navigation: NavHostController
) {
    val datePickerVisibility = remember { mutableStateMapOf<Int, Boolean>() }
    var selectedDate by remember { mutableStateOf("") }

    val personalInformationViewModel =
        hiltViewModel<PersonalInformationViewModel, PersonalInformationViewModel.PersonalInformationViewModelFactory> {
            it.create(appViewModel.user)
        }

    val personalInfoState by personalInformationViewModel.personalInfo.collectAsState()

    if (personalInformationViewModel.state.isNecessaryDataFetching) {
        LoaderPage()
        return
    }

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
        Column(modifier = Modifier.fillMaxSize()) {
            when (personalInfoState) {
                is PersonalInfoUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                }

                is PersonalInfoUiState.Success -> {
                    val info = (personalInfoState as PersonalInfoUiState.Success).data
                    var showBottomSheet by remember { mutableStateOf(false) }

                    // BottomSheetSelectableImageSourceAuto placed outside of LazyColumn
                    if (showBottomSheet) {
                        BottomSheetSelectableImageSourceAuto(
                            onImageObtained = { uri ->
                                personalInformationViewModel.updateProfilePhoto(uri)
                                showBottomSheet = false
                                // Handle the selected image URI here if needed
                            },
                            isVisible = showBottomSheet,
                            onVisibilityChange = { showBottomSheet = it },
                            trigger = {
                                // Optionally include a trigger UI here, but it's not necessary
                            }
                        )
                    }

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        item {
                            ProfilePhoto(
                                if (personalInformationViewModel.state.photo == null) {
                                    appViewModel.user?.profilePhoto.toString()
                                } else {
                                    personalInformationViewModel.state.photo.toString()
                                },
                                onPhotoClick = {
                                    showBottomSheet = true
                                })
                        }


                        items(info.size) { index ->

                            val item = info[index]
                            val value = item.value
                            val label = item.label

                            val showDatePicker = datePickerVisibility[index] ?: false
                            if (label == "Names" || label == "Surnames" || label == "Identification Document" ||
                                label == "Email" || label == "Phone" || label == "Birthdate"
                            ) {
                                PersonalInfoItem(
                                    label = label,
                                    value = value,
                                    isValueSelected = item.isValueSelected,
                                    onClick = {
                                        if (item.label == "Birthdate") {
                                            datePickerVisibility[index] = true
                                        }
                                    }
                                )
                            } else {
                                when (label) {
                                    "Document type" -> {
                                        Spacer(modifier = Modifier.width(16.dp))
                                        SelectableBottomSheet(
                                            elements = personalInformationViewModel.state.documentTypes,
                                            error = personalInformationViewModel.state.documentTypesError,
                                            onChange = {
                                                personalInformationViewModel.updateDocumentType(
                                                    it
                                                )
                                            },
                                            placeholder = stringResource(R.string.placeholder_document_type),
                                            title = stringResource(R.string.document_type_title),
                                            value = personalInformationViewModel.state.selectedDocumentType
                                        )
                                    }

                                    "Department" -> {
                                        SelectableBottomSheet(
                                            elements = personalInformationViewModel.state.documentTypes,
                                            error = personalInformationViewModel.state.documentTypesError,
                                            onChange = {
                                                personalInformationViewModel.updateDocumentType(
                                                    it
                                                )
                                            },
                                            placeholder = stringResource(R.string.department),
                                            title = stringResource(R.string.department_type_title),
                                            value = personalInformationViewModel.state.selectedDocumentType
                                        )
                                    }

                                    "Country" -> {
                                        Spacer(Modifier.height(5.dp))
                                        SelectableBottomSheet(
                                            elements = personalInformationViewModel.state.countries,
                                            error = personalInformationViewModel.state.documentTypesError,
                                            onChange = {
                                                personalInformationViewModel.updateCountry(
                                                    it
                                                )
                                            },
                                            placeholder = stringResource(R.string.country),
                                            title = stringResource(R.string.country_title),
                                            value = personalInformationViewModel.state.selectedCountry
                                        )
                                    }

                                    "Province" -> {
                                        SelectableBottomSheet(
                                            elements = personalInformationViewModel.state.countries,
                                            error = personalInformationViewModel.state.documentTypesError,
                                            onChange = {
//                                                personalInformationViewModel.updateDocumentType(
//                                                    it
//                                                )
                                            },
                                            placeholder = stringResource(R.string.province),
                                            title = stringResource(R.string.province_title),
                                            value = personalInformationViewModel.state.selectedDocumentType
                                        )
                                    }
                                }
                            }

                            if (showDatePicker) {
                                DatePicker(
                                    onDateSelected = { timestamp ->
                                        if (timestamp != null) {
                                            // Convert timestamp to your desired format
                                            selectedDate = DateTimeUtils.parse(
                                                timestamp, "MMMM dd, yyyy", TimeZone.getDefault()
                                            )
                                        }
                                        datePickerVisibility[index] =
                                            false // Hide DatePicker after selection
                                    },
                                    onDismissRequest = {
                                        datePickerVisibility[index] = false // Hide on dismiss
                                    }
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            personalInformationViewModel.storePersonalInformation()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        text = stringResource(id = R.string.save),
                    )
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
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    onDateSelected: (Long?) -> Unit,
    onDismissRequest: () -> Unit
) {
    val todayMillis = System.currentTimeMillis()
    val datePickerState = rememberDatePickerState()
    val handleDateState = { selectedDateMillis: Long? ->
        if (selectedDateMillis != null && selectedDateMillis > todayMillis) {
            onDateSelected(todayMillis)
        } else {
            onDateSelected(selectedDateMillis)
        }
    }
    DatePickerDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = {
                handleDateState(datePickerState.selectedDateMillis)
                onDismissRequest()
            }) {
                Text(text = stringResource(id = R.string.button_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = stringResource(id = R.string.button_cancel))
            }
        }
    ) {
        androidx.compose.material3.DatePicker(state = datePickerState)
    }
}
