package com.example.logifitappp.viewmodel.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.enums.CountryPhoneCodeEnum
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = PhoneTextFieldViewModel.PhoneTextFieldViewModelFactory::class)
class PhoneTextFieldViewModel @AssistedInject constructor (
    @Assisted private val providedValue: String
): ViewModel() {
    @AssistedFactory
    interface PhoneTextFieldViewModelFactory {
        fun create(providedValue: String): PhoneTextFieldViewModel
    }

    var code by mutableStateOf(CountryPhoneCodeEnum.findByLocaleSystem())
    var text by mutableStateOf(TextFieldValue(""))
    var isCountryPhoneCodeBottomSheetVisible by mutableStateOf(false)

    init {
        if (providedValue.isNotEmpty()) {
            CountryPhoneCodeEnum.findByPhone(providedValue)?.let {
                code = it
                text = TextFieldValue(providedValue.replace(it.code, ""))
            }
        }
    }
}