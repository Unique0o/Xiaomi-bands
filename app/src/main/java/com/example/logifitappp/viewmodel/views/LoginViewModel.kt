package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.R
import com.example.logifitappp.core.App.Companion.context
import com.example.logifitappp.domain.usecase.LoginUseCase
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.example.logifitappp.viewmodel.views.Authentication.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase

) : ViewModel() {
    var isLogin by mutableStateOf(false)
        private set

    var password by mutableStateOf(TextFieldValue(""))
        private set

    var username by mutableStateOf(TextFieldValue(""))
        private set

    var uiState by mutableStateOf(LoginUiState())
        private set

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin


    fun login(onLoginSuccess: (Boolean) -> Unit) {
        if (!validateInputsNotEmpty()) {
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                val user = loginUseCase(username.text, password.text)
                println("User: $user")
                val adminStatus = user.isAdmin()
                _isAdmin.value = adminStatus
                uiState = uiState.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    user = user
                )
                onLoginSuccess(adminStatus)
            } catch (e: HttpConsumerException) {
                handleLoginError(e)
            }
        }
    }

    private fun handleLoginError(e: HttpConsumerException) {
        println("Error code: ${e.getStatus().code()}")
        val (usernameError, passwordError) = when (e.getStatus()) {
            AppStatusCodeEnum.UNREGISTERED_USER -> Pair(context.getString(R.string.unregistered_user_error_message), null)
            AppStatusCodeEnum.PASSWORD_NOT_VALIDATED -> Pair(null, context.getString(R.string.password_not_validated_error_message))
            else -> Pair(null, null)
        }

        uiState = uiState.copy(
            isLoading = false,
            usernameError = usernameError,
            passwordError = passwordError,
            error = if (usernameError == null && passwordError == null) context.getString(R.string.error_general) else null
        )

    }

    private fun validateInputsNotEmpty(): Boolean {
        val isUsernameValid = username.text.isNotBlank()
        val isPasswordValid = password.text.isNotBlank()

        uiState = uiState.copy(
            usernameError = if (isUsernameValid) null else context.getString(R.string.error_username_empty),
            passwordError = if (isPasswordValid) null else context.getString(R.string.error_password_empty)
        )

        return isUsernameValid && isPasswordValid
    }

    private fun clearErrors() {
        uiState = uiState.copy(error = null, usernameError = null, passwordError = null)
    }
    fun updatePassword(password: TextFieldValue) {
        this.password = password
        clearErrors()
    }

    fun updateUsername(username: TextFieldValue) {
        this.username = username
        clearErrors()
    }
}