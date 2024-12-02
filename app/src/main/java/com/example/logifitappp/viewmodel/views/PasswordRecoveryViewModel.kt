package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.R
import com.example.logifitappp.core.App.Companion.context
import com.example.logifitappp.domain.service.AuthService
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.example.logifitappp.viewmodel.states.PasswordRecoveryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {
    var state by mutableStateOf(PasswordRecoveryState())
        private set

    fun recoverPassword() {
        if (!validateInputsNotEmpty()) return

        viewModelScope.launch {
            state = state.copy(
                isPasswordRecovering = true,
                status = AppStatusCodeEnum.RECOVERING_PASSWORD
            )

            try {
                authService.recoverPassword(state.username.text)

                state = state.copy(
                    hasPasswordRecoveryBeenSuccessful = true,
                    status = AppStatusCodeEnum.SUCCESSFUL_PASSWORD_RECOVERY
                )
            } catch (e: HttpConsumerException) {
                state = state.copy(
                    hasPasswordRecoveryFailed = true,
                    status = e.getStatus()
                )
            } finally {
                state = state.copy(isPasswordRecovering = false)
            }
        }
    }

    fun stopProcessing() {
        state = state.copy(
            hasPasswordRecoveryBeenSuccessful = false,
            hasPasswordRecoveryFailed = false,
            isPasswordRecovering = false
        )
    }

    fun updateUsername(username: TextFieldValue) {
        state = state.copy(username = username)
    }

    private fun validateInputsNotEmpty(): Boolean {
        return if (state.username.text.isBlank()) false.also {
            state = state.copy(usernameError = context.getString(R.string.username_validation_error_message))
        } else true
    }
}