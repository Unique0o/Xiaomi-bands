package com.example.logifitappp.viewmodel.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.core.App
import com.example.logifitappp.core.AppPreferences

class BottomTabsHeaderViewModel: ViewModel() {
    var hasNewNotification by mutableStateOf(false)
        private set

    fun checkForNewNotification() {
        println("checkForNewNotification ${ App.preferences.getPreferences().getBoolean(AppPreferences.NEW_NOTIFICATION, false)}")
        hasNewNotification = App.preferences.getPreferences().getBoolean(AppPreferences.NEW_NOTIFICATION, false)
    }
}