package com.example.logifitappp.ui.components.forms

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField as MaterialOutlinedTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.data.models.CountryPhoneCode
import com.example.logifitappp.ui.components.items.CountryCodeItem
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneTextInput(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    countryCodes: List<CountryPhoneCode>,
    error: String? = null
) {
    var selectedCountry by remember { mutableStateOf(countryCodes.first()) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    MaterialOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors().copy(
            cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
            errorContainerColor = Color.Transparent,
            errorIndicatorColor = MaterialTheme.colorScheme.error,
            errorLabelColor = MaterialTheme.colorScheme.error,
            errorLeadingIconColor = MaterialTheme.colorScheme.error,
            errorSupportingTextColor = MaterialTheme.colorScheme.error,
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
            unfocusedLabelColor = MaterialTheme.colorScheme.surfaceTint,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.surfaceTint
        ),
        isError = !error.isNullOrEmpty(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        label = {
            Text(
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight,
                text = stringResource(id = R.string.placeholder_phone)
            )
        },
        leadingIcon = {
            Row(
                modifier = Modifier
                    .clickable { showBottomSheet = true }
                    .padding(start = 8.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedCountry.id != "EMPTY") {
                    selectedCountry.flagResId?.let { flagResId ->
                        Image(
                            painter = painterResource(id = flagResId),
                            contentDescription = "Country flag",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = selectedCountry.code,
                        modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                    )
                } else {
                    Text(
                        text = selectedCountry.name,
                        modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select country",
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        supportingText = {
            if (!error.isNullOrEmpty()) {
                Text(
                    fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    lineHeight = MaterialTheme.typography.labelSmall.lineHeight,
                    modifier = Modifier.fillMaxWidth(),
                    text = error,
                    textAlign = TextAlign.Right
                )
            }
        },
        textStyle = MaterialTheme.typography.bodyMedium
    )

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            LazyColumn {
                items(countryCodes) { country ->
                    CountryCodeItem(
                        country = country,
                        isSelected = country == selectedCountry,
                        onSelect = {
                            selectedCountry = country
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun MyScreen() {
    var phoneNumber by remember { mutableStateOf(TextFieldValue(" ")) }
    val countryCodes = remember { getCountryCodes() }

    PhoneTextInput(
        value = phoneNumber,
        onValueChange = { phoneNumber = it },
        countryCodes = countryCodes
    )
}

fun getCountryCodes(): List<CountryPhoneCode> {
    return listOf(
        CountryPhoneCode("Seleccionar país", "", null, "EMPTY"),
        CountryPhoneCode("Perú", "+51", R.drawable.peru, "PE/PER"),
        CountryPhoneCode("Costa Rica", "+506", R.drawable.costa_rica, "CR/CRC"),
    )
}