package com.example.logifitappp.ui.screens.personal_information


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.data.models.PersonalInfoModel
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

                    PersonalInfoScreen(info, appViewModel, personalInformationViewModel)
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


@Composable
fun PersonalInfoScreen(
    info: List<PersonalInfoModel>,
    appViewModel: AppViewModel,
    personalInformationViewModel: PersonalInformationViewModel
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    val datePickerVisibility = remember { mutableStateMapOf<Int, Boolean>() }
    var updatedInfo by rememberSaveable { mutableStateOf(info) }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
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
            items(updatedInfo.size) { index ->
                val item = updatedInfo[index]
                val label = item.label

                if (label == "Names" || label == "Surnames" || label == "Identification Document" ||
                    label == "Email" || label == "Phone" || label == "Birthdate"
                ) {
                    PersonalInfoItem(
                        label = label,
                        value = item.value,
                        isValueSelected = item.isValueSelected,
                        onValueChange = {
//                            updatedInfo = updatedInfo.toMutableList().apply {
//                                this[index] = this[index].copy(value = it.toString())
//                            }
                        },
                        onClick = {
                            if (item.label == "Birthdate") {
                                datePickerVisibility[index] = true
                            }
                        }
                    )
                } else {
                    when (label) {
                        "Document type" -> DocumentTypeSection(personalInformationViewModel)
                        "Country" -> CountrySection(personalInformationViewModel)
                        "Department" -> DepartmentSection(personalInformationViewModel)
                        "Province" -> ProvinceSection(personalInformationViewModel)
                    }
                }

                // Show DatePicker if necessary
                // Show DatePicker if necessary
                if (datePickerVisibility[index] == true) {

                    var selectedDate by remember { mutableStateOf("") }
                    var selectedDateMillis by rememberSaveable { mutableStateOf<Long?>(null) }  // Store the selected date in state


                    DatePicker(
                        selectedDateMillis = selectedDateMillis,
                        onDateSelected = { timestamp ->
                            if (timestamp != null) {
                                // Convert timestamp to your desired format
                                selectedDateMillis = timestamp
                                selectedDate = DateTimeUtils.parse(
                                    timestamp, "MMMM dd, yyyy", TimeZone.getDefault()
                                )
                                updatedInfo = updatedInfo.toMutableList().apply {
                                    this[index] = this[index].copy(value = selectedDate)
                                }
                            }
                            datePickerVisibility[index] = false // Hide DatePicker after selection
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
}

// Document Type Section
@Composable
fun DocumentTypeSection(personalInformationViewModel: PersonalInformationViewModel) {
    Spacer(modifier = Modifier.width(16.dp))
    SelectableBottomSheet(
        elements = personalInformationViewModel.state.documentTypes,
        error = personalInformationViewModel.state.documentTypesError,
        onChange = {
            personalInformationViewModel.updateDocumentType(it)
        },
        placeholder = stringResource(R.string.placeholder_document_type),
        title = stringResource(R.string.document_type_title),
        value = personalInformationViewModel.state.selectedDocumentType
    )
}

// Country Section
@Composable
fun CountrySection(personalInformationViewModel: PersonalInformationViewModel) {
    Spacer(Modifier.height(5.dp))
    SelectableBottomSheet(
        elements = personalInformationViewModel.state.countries,
        error = personalInformationViewModel.state.countriesError,
        onChange = {
            personalInformationViewModel.updateCountry(it)
        },
        placeholder = stringResource(R.string.country),
        title = stringResource(R.string.country_title),
        value = personalInformationViewModel.state.selectedCountry
    )
}

// Department Section
@Composable
fun DepartmentSection(personalInformationViewModel: PersonalInformationViewModel) {
    key(personalInformationViewModel.state.departments) {
        SelectableBottomSheet(
            elements = personalInformationViewModel.state.departments,
            error = personalInformationViewModel.state.departmentTypesError,
            onChange = {
                personalInformationViewModel.updateDepartment(it)
            },
            placeholder = stringResource(R.string.department),
            title = stringResource(R.string.department_type_title),
            value = personalInformationViewModel.state.selectedDepartment
        )
    }
}

// Province Section
@Composable
fun ProvinceSection(personalInformationViewModel: PersonalInformationViewModel) {
    key(personalInformationViewModel.state.provinces) {
        SelectableBottomSheet(
            elements = personalInformationViewModel.state.provinces,
            error = personalInformationViewModel.state.documentTypesError,
            onChange = {
                personalInformationViewModel.updateProvince(it)
            },
            placeholder = stringResource(R.string.province),
            title = stringResource(R.string.province_title),
            value = personalInformationViewModel.state.selectedProvince
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    selectedDateMillis: Long?,
    onDateSelected: (Long?) -> Unit,
    onDismissRequest: () -> Unit
) {
    val todayMillis = System.currentTimeMillis()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDateMillis
            ?: todayMillis  // Initialize picker with selected date
    )
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
