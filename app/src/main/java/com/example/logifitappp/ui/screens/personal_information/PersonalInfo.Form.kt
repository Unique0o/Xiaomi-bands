package com.example.logifitappp.ui.screens.personal_information

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.ui.components.BottomSheetSelectableImageSourceAuto
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.components.forms.ClickableOutlinedTextWithCustomPlaceholder
import com.example.logifitappp.ui.components.forms.OutlinedTextField
import com.example.logifitappp.ui.components.forms.PhoneTextField
import com.example.logifitappp.ui.components.forms.SelectableBottomSheet
import com.example.logifitappp.ui.components.personalInformation.ProfilePhoto
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.PersonalInformationViewModel
import java.util.TimeZone

@Composable
fun PersonalInfoScreen(
    appViewModel: AppViewModel,
    personalInformationViewModel: PersonalInformationViewModel
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var datePickerVisibility by remember { mutableStateOf(false) }
    var selectedDate by rememberSaveable {
        mutableStateOf(
            personalInformationViewModel.state.birthdate ?: ""
        )
    }

    if (showBottomSheet) {
        BottomSheetSelectableImageSourceAuto(
            onImageObtained = { uri ->
                personalInformationViewModel.updateProfilePhoto(uri)
                showBottomSheet = false
            },
            isVisible = showBottomSheet,
            onVisibilityChange = { showBottomSheet = it },
            trigger = {

            }
        )
    }

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    personalInformationViewModel.storeProfilePhoto()
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                text = stringResource(id = R.string.save),
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                ProfilePhoto(
                    if (personalInformationViewModel.state.photo == null) {
                        appViewModel.user?.profilePhoto.toString()
                    } else {
                        personalInformationViewModel.state.photo.toString()
                    },
                    onPhotoClick = { showBottomSheet = true }
                )

                Spacer(Modifier.height(15.dp))

                OutlinedTextField(
                    error = personalInformationViewModel.state.namesError,
                    onValueChange = { personalInformationViewModel.updateNames(it) },
                    placeholder = stringResource(R.string.placeholder_names),
                    value = personalInformationViewModel.state.names,
                    keyboardOption = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text // Opens numeric keyboard
                    ),
                )

                OutlinedTextField(
                    error = personalInformationViewModel.state.surnamesError,
                    onValueChange = { personalInformationViewModel.updateSurnames(it) },
                    placeholder = stringResource(R.string.placeholder_surnames),
                    value = personalInformationViewModel.state.surnames,
                    keyboardOption = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text // Opens numeric keyboard
                    )
                )

                OutlinedTextField(
                    error = personalInformationViewModel.state.emailError,
                    onValueChange = { personalInformationViewModel.updateEmail(it) },
                    placeholder = stringResource(R.string.email),
                    value = personalInformationViewModel.state.email,
                    keyboardOption = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email // Opens numeric keyboard
                    )
                )

                OutlinedTextField(
                    error = personalInformationViewModel.state.identityDocumentError,
                    onValueChange = { personalInformationViewModel.updateIdentityDocument(it) },
                    placeholder = stringResource(R.string.placeholder_identity_document),
                    value = personalInformationViewModel.state.identityDocument
                )

                OutlinedTextField(
                    error = personalInformationViewModel.state.phoneError,
                    onValueChange = { personalInformationViewModel.updatePhone(it) },
                    placeholder = stringResource(R.string.phone),
                    value = personalInformationViewModel.state.phone,
                    keyboardOption = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone // Opens numeric keyboard
                    )
                )

                ClickableOutlinedTextWithCustomPlaceholder(
                    error = personalInformationViewModel.state.birthdateError,
                    value = personalInformationViewModel.state.birthdate.ifEmpty { stringResource(R.string.birthdate) },
                    onClick = { datePickerVisibility = true }
                )

                Spacer(modifier = Modifier.height(15.dp))

                DocumentTypeSection(personalInformationViewModel)
                CountrySection(personalInformationViewModel)
                DepartmentSection(personalInformationViewModel)
                ProvinceSection(personalInformationViewModel)

                if (datePickerVisibility) {
                    var selectedDateMillis by rememberSaveable { mutableStateOf<Long?>(null) }

                    DatePicker(
                        selectedDateMillis = selectedDateMillis,
                        onDateSelected = { timestamp ->
                            if (timestamp != null) {
                                selectedDateMillis = timestamp
                                selectedDate = DateTimeUtils.parse(
                                    timestamp, "yyyy-MM-dd", TimeZone.getDefault()
                                )
                                personalInformationViewModel.updateBirthdate(selectedDate)
                                personalInformationViewModel.updateBirthdateError("")
                            }
                            datePickerVisibility = false
                        },
                        onDismissRequest = { datePickerVisibility = false }
                    )
                }

                Spacer(modifier = Modifier.height(80.dp)) // To prevent last field from being hidden behind the button
            }
        }
    }
}

// Document Type Section
@Composable
fun DocumentTypeSection(personalInformationViewModel: PersonalInformationViewModel) {
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
            error = personalInformationViewModel.state.provinceError,
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

