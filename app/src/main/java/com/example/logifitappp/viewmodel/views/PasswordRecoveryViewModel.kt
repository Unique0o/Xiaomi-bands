package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel

class PasswordRecoveryViewModel: ViewModel() {
    var username by mutableStateOf(TextFieldValue(""))
        private set

    fun updateUsername(username: TextFieldValue) {
        this.username = username
    }
}