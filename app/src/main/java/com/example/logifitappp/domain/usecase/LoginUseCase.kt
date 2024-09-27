package com.example.logifitappp.domain.usecase

import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.data.remote.dto.requests.LoginRequest
import com.example.logifitappp.domain.service.AuthService
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authService: AuthService) {
    suspend operator fun invoke(nick: String, password: String): UserModel {
        return authService.login(LoginRequest(nick, password))
    }
}