package com.example.logifitappp.viewmodel.states

import androidx.compose.ui.text.input.TextFieldValue
import com.example.logifitappp.data.models.CountryModel
import com.example.logifitappp.data.models.DepartmentModel
import com.example.logifitappp.data.models.DocumentTypeModel
import com.example.logifitappp.data.models.ProvinceModel
import com.example.logifitappp.enums.AppStatusCodeEnum

data class AdditionalInformationState(
    val countries: List<CountryModel> = listOf(),
    val countriesError: String? = null,
    val currentPage: Int = 0,
    val departments: List<DepartmentModel>? = null,
    val documentTypes: List<DocumentTypeModel> = listOf(),
    val documentTypesError: String? = null,
    val hasNecessaryDataFetchingFailed: Boolean = false,
    val identityDocument: TextFieldValue = TextFieldValue(""),
    val identityDocumentError: String? = null,
    val isInformationStoring: Boolean = false,
    val isNecessaryDataFetching: Boolean = true,
    val names: TextFieldValue = TextFieldValue(""),
    val namesError: String? = null,
    val phone: TextFieldValue = TextFieldValue(""),
    val phoneError: String? = null,
    val photo: Any? = null,
    val photoError: String? = null,
    val provinces: List<ProvinceModel>? = null,
    val selectedCountry: CountryModel? = null,
    val selectedDocumentType: DocumentTypeModel? = null,
    val storingInformationStatus: AppStatusCodeEnum = AppStatusCodeEnum.STORING_ADDITIONAL_INFORMATION,
    val surnames: TextFieldValue = TextFieldValue(""),
    val surnamesError: String? = null
)
