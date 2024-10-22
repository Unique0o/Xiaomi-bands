package com.example.logifitappp.viewmodel.states

import androidx.compose.ui.text.input.TextFieldValue
import com.example.logifitappp.enums.AppStatusCodeEnum

data class PasswordRecoveryState(
    val hasPasswordRecoveryBeenSuccessful: Boolean = false,
    val hasPasswordRecoveryFailed: Boolean = false,
    val isPasswordRecovering: Boolean = false,
    val status: AppStatusCodeEnum = AppStatusCodeEnum.RECOVERING_PASSWORD,
    val username: TextFieldValue = TextFieldValue(""),
    val usernameError: String? = null
)
