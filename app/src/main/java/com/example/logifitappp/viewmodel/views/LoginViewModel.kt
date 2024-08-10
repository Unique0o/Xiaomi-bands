package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    var password by mutableStateOf(TextFieldValue(""))
        private set

    var username by mutableStateOf(TextFieldValue(""))
        private set

    fun updatePassword(password: TextFieldValue) {
        this.password = password
    }

    fun updateUsername(username: TextFieldValue) {
        this.username = username
    }
}