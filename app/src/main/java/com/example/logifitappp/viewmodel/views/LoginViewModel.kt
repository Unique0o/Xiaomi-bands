package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.di.services.AuthService
import com.example.logifitappp.di.services.requests.LoginRequest
import com.example.logifitappp.exceptions.HttpConsumerException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {
    var isLogin by mutableStateOf(false)
        private set

    var password by mutableStateOf(TextFieldValue(""))
        private set

    var username by mutableStateOf(TextFieldValue(""))
        private set

    fun login() {
        viewModelScope.launch {
            try {
                val user = authService.login(LoginRequest(username.text, password.text))

                println(user)
            } catch (e: HttpConsumerException) {
                println(e)
            }
        }
    }

    fun updatePassword(password: TextFieldValue) {
        this.password = password
    }

    fun updateUsername(username: TextFieldValue) {
        this.username = username
    }
}