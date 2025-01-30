package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.domain.usecase.LoginUseCase
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.example.logifitappp.viewmodel.states.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
): ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    private fun clearForm() {
        state = state.copy(
            username = TextFieldValue(""),
            password = TextFieldValue("")
        )
    }

    fun login() {
        if (!validateInputsNotEmpty()) return

        viewModelScope.launch {
            try {
                state = state.copy(
                    isLoggedIn = true,
                    status = AppStatusCodeEnum.LOGGING_IN
                )

                loginUseCase(state.username.text, state.password.text)
                App.signalReloadAuthenticatedUser()

                clearForm()
            } catch (e: HttpConsumerException) {
                state = state.copy(
                    hasLoginProcessFailed = true,
                    status = e.getStatus()
                )
            } finally {
                state = state.copy(isLoggedIn = false)
            }
        }
    }

    fun stopProcessing() {
        state = state.copy(
            hasLoginProcessFailed = false,
            isLoggedIn = false
        )
    }

    fun updatePassword(password: TextFieldValue) {
        state = state.copy(password = password)
    }

    fun updateUsername(username: TextFieldValue) {
        state = state.copy(username = username)
    }

    private fun validateInputsNotEmpty(): Boolean {
        val isUsernameValid = state.username.text.isNotBlank()
        val isPasswordValid = state.password.text.isNotBlank()

        state = state.copy(
            usernameError = if (isUsernameValid) null else App.context.getString(R.string.username_validation_error_message),
            passwordError = if (isPasswordValid) null else App.context.getString(R.string.password_validation_error_message)
        )

        return isUsernameValid && isPasswordValid
    }
}