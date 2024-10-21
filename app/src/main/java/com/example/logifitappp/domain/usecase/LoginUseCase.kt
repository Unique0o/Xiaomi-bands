package com.example.logifitappp.domain.usecase

import com.example.logifitappp.core.App
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.data.remote.dto.requests.LoginRequest
import com.example.logifitappp.domain.service.AuthService
import com.example.logifitappp.exceptions.HttpConsumerException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authService: AuthService,
    private val updateTenantInformationUseCase: UpdateTenantInformationUseCase
) {
    suspend operator fun invoke(nick: String, password: String): UserModel {
        val user = authService.login(LoginRequest(nick, password))
        val userDao = App.database.userDao()

        try {
            userDao.store(user)
            updateTenantInformationUseCase()
        } catch (e: HttpConsumerException) {
            userDao.deleteLoggedIn()
        }

        return user
    }
}