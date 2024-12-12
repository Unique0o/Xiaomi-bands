package com.example.logifitappp.ui.screens.additionalInformation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.components.forms.OutlinedTextField
import com.example.logifitappp.ui.components.forms.PhoneTextInput
import com.example.logifitappp.viewmodel.views.AdditionalInformation.AdditionalInformationViewModel


@Composable
fun AdditionalInformationForm(
    additionalInformationViewModel: AdditionalInformationViewModel,
    onSubmit: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val state by additionalInformationViewModel.state.collectAsState()

    val countryCodes = remember { additionalInformationViewModel.getCountryCodes() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.first_additional_information_title),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.first_additional_information_message),
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            value = state.name,
            onValueChange = { additionalInformationViewModel.updateName(it) },
            placeholder = stringResource(id = R.string.placeholder_name),
        )
        OutlinedTextField(
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            ),
            onValueChange = {
                additionalInformationViewModel.updateLastnames(it)
            },
            placeholder = stringResource(id = R.string.placeholder_lastname),
            value = state.lastnames
        )

        /*SelectableBottomSheetList(
            items = state.countries,
            value = state.selectedCountry,
            onSelectItem = { additionalInformationViewModel.selectCountry(it) },
            placeholder = stringResource(R.string.placeholder_type_document),
            title = stringResource(R.string.placeholder_country),
            errorLabel = state.error,
        )*/
        OutlinedTextField(
            keyboardActions = KeyboardActions(
                onDone = {

                }
            ),
            onValueChange = {
                additionalInformationViewModel.updateDocumentIdentity(it)
            },
            placeholder = stringResource(id = R.string.placeholder_document_identity),
            value = state.documentIdentity
        )
        /*SelectableBottomSheetList(
            items = state.countries,
            value = state.selectedCountry,
            onSelectItem = { additionalInformationViewModel.selectCountry(it) },
            placeholder = stringResource(R.string.placeholder_country),
            title = stringResource(R.string.placeholder_country),
            errorLabel = state.error,
        )*/


        PhoneTextInput(
            value = state.mobile,
            onValueChange = { additionalInformationViewModel.updateMobile(it) },
            countryCodes = countryCodes
        )

        IconButton(
            icon = Icons.AutoMirrored.Rounded.Send,
            onClick = {additionalInformationViewModel.onSubmitPersonalInformation { onSubmit() } },
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.button_continue)
        )
    }
}

/*fun CountryModel.toSelectableItem() = object : SelectableItem {
    override val name: String = this@toSelectableItem.name
}*/

