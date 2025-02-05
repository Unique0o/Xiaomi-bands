package com.example.logifitappp.viewmodel.states

import androidx.compose.ui.text.input.TextFieldValue
import com.example.logifitappp.enums.AppStatusCodeEnum

data class LoginState(
    val hasLoginProcessFailed: Boolean = false,
    val isLoggedIn: Boolean = false,
    val password: TextFieldValue = TextFieldValue(""),
    val passwordError: String? = null,
    val status: AppStatusCodeEnum = AppStatusCodeEnum.LOGGING_IN,
    val username: TextFieldValue = TextFieldValue(""),
    val usernameError: String? = null
)