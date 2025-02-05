package com.example.logifitappp.ui.screens.additional_information

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.components.forms.OutlinedTextField
import com.example.logifitappp.ui.components.forms.PhoneTextField
import com.example.logifitappp.ui.components.forms.SelectableBottomSheet
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.viewmodel.views.AdditionalInformationViewModel

@Composable
fun AdditionalInformationForm(
    additionalInformationViewModel: AdditionalInformationViewModel
) {
    SimplePage {
        Text(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.first_additional_information_title),
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.displayLarge
        )

        Spacer(Modifier.height(2.dp))

        Text(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.first_additional_information_message),
            textAlign = TextAlign.Center,
            typography = MaterialTheme.typography.labelMedium
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            error = additionalInformationViewModel.state.namesError,
            onValueChange = { additionalInformationViewModel.updateNames(it) },
            placeholder = stringResource(R.string.placeholder_names),
            value = additionalInformationViewModel.state.names
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            error = additionalInformationViewModel.state.surnamesError,
            onValueChange = { additionalInformationViewModel.updateSurnames(it) },
            placeholder = stringResource(R.string.placeholder_surnames),
            value = additionalInformationViewModel.state.surnames
        )

        Spacer(Modifier.height(12.dp))

        SelectableBottomSheet(
            elements = additionalInformationViewModel.state.documentTypes,
            error = additionalInformationViewModel.state.documentTypesError,
            onChange = { additionalInformationViewModel.updateDocumentType(it) },
            placeholder = stringResource(R.string.placeholder_document_type),
            title = stringResource(R.string.document_type_title),
            value = additionalInformationViewModel.state.selectedDocumentType
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            error = additionalInformationViewModel.state.identityDocumentError,
            onValueChange = { additionalInformationViewModel.updateIdentityDocument(it) },
            placeholder = stringResource(R.string.placeholder_identity_document),
            value = additionalInformationViewModel.state.identityDocument
        )

        Spacer(Modifier.height(12.dp))

        SelectableBottomSheet(
            elements = additionalInformationViewModel.state.countries,
            error = additionalInformationViewModel.state.countriesError,
            onChange = { additionalInformationViewModel.updateCountry(it) },
            placeholder = stringResource(R.string.placeholder_country),
            title = stringResource(R.string.country_title),
            value = additionalInformationViewModel.state.selectedCountry
        )

        Spacer(Modifier.height(12.dp))

        PhoneTextField(
            error = additionalInformationViewModel.state.phoneError,
            onValueChange = { additionalInformationViewModel.updatePhone(it) },
            value = additionalInformationViewModel.state.phone
        )

        Spacer(Modifier.weight(1f))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { additionalInformationViewModel.storePersonalInformation() },
            text = stringResource(R.string.button_continue)
        )
    }
}