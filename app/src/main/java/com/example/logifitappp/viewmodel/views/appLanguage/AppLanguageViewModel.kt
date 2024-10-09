package com.example.logifitappp.viewmodel.views.appLanguage


import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AppLanguageViewModel @Inject constructor(
) : ViewModel() {

    private val _currentLanguage = MutableStateFlow(getCurrentLanguage())
    val currentLanguage: StateFlow<String> = _currentLanguage

    private fun getCurrentLanguage(): String {
        return AppCompatDelegate.getApplicationLocales().get(0)?.language ?: "en"
    }

    fun setLanguage(languageTag: String) {
        viewModelScope.launch {
            val localeList = LocaleListCompat.forLanguageTags(languageTag)
            AppCompatDelegate.setApplicationLocales(localeList)
            _currentLanguage.value = languageTag
        }
    }
}