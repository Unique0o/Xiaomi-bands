package com.example.logifitappp.ui.components.forms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.logifitappp.R
import com.example.logifitappp.enums.CountryPhoneCodeEnum
import com.example.logifitappp.enums.CountryPhoneCodeSelectableItem
import com.example.logifitappp.ui.components.BottomSheetSearchable
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.viewmodel.components.PhoneTextFieldViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneTextField(
    modifier: Modifier = Modifier,
    error: String? = null,
    onValueChange: (TextFieldValue) -> Unit,
    value: TextFieldValue
) {
    val phoneTextFieldViewModel = hiltViewModel<PhoneTextFieldViewModel, PhoneTextFieldViewModel.PhoneTextFieldViewModelFactory>{
        it.create(value.text)
    }

    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val toggleModalBottomSheet = {
        coroutineScope.launch {
            if (phoneTextFieldViewModel.isCountryPhoneCodeBottomSheetVisible) modalBottomSheetState.hide()
            else modalBottomSheetState.show()
        }.invokeOnCompletion {
            phoneTextFieldViewModel.isCountryPhoneCodeBottomSheetVisible = !phoneTextFieldViewModel.isCountryPhoneCodeBottomSheetVisible
        }
    }

    BottomSheetSearchable(
        coroutineScope = coroutineScope,
        elements = CountryPhoneCodeEnum.toBottomSheetSelectableItems(),
        isVisible = phoneTextFieldViewModel.isCountryPhoneCodeBottomSheetVisible,
        modalBottomSheetState = modalBottomSheetState,
        onChange = {
            phoneTextFieldViewModel.code = it.self
            onValueChange(TextFieldValue("${it.self.code}${phoneTextFieldViewModel.text.text}"))
        },
        onDismissRequest = { phoneTextFieldViewModel.isCountryPhoneCodeBottomSheetVisible = false },
        renderItem = {
            val isSelected = it.id == phoneTextFieldViewModel.code?.ordinal

            Row {
                Image(
                    contentDescription = "country phone code",
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(it.self.icon)
                )

                Text(
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    modifier = Modifier.weight(1f).padding(start = 8.dp, end = 16.dp),
                    overflow = TextOverflow.Ellipsis,
                    text = it.toString(),
                    typography = MaterialTheme.typography.headlineLarge
                )

                if (isSelected) {
                    Icon(
                        contentDescription = null,
                        imageVector = Icons.Filled.Check,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        title = stringResource(R.string.country_phone_code_title),
        toggleModalBottomSheet = toggleModalBottomSheet,
        value = phoneTextFieldViewModel.code?.let { return@let CountryPhoneCodeSelectableItem(it.ordinal, it) }
    )

    OutlinedTextField(
        error = error,
        leadingComponent = {
            Row(
                Modifier.clickable { toggleModalBottomSheet() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(8.dp))

                if (phoneTextFieldViewModel.code != null) {
                    Image(
                        contentDescription = "country phone code",
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(phoneTextFieldViewModel.code!!.icon)
                    )

                    Spacer(Modifier.width(4.dp))
                }

                Text(
                    color = MaterialTheme.colorScheme.onSurface,
                    text = phoneTextFieldViewModel.code?.code ?: "-",
                    typography = MaterialTheme.typography.labelMedium
                )

                Spacer(Modifier.width(8.dp))
                Box(Modifier.width(1.dp).height(12.dp).background(MaterialTheme.colorScheme.outline))
                Spacer(Modifier.width(8.dp))
            }
        },
        modifier = modifier,
        onValueChange = {
            phoneTextFieldViewModel.text = it
            onValueChange(TextFieldValue("${phoneTextFieldViewModel.code?.code ?: ""}${it.text}"))
        },
        placeholder = stringResource(R.string.placeholder_phone_number),
        value = phoneTextFieldViewModel.text,
        keyboardOption = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Phone
        )
    )
}