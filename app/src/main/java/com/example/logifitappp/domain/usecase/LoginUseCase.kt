package com.example.logifitappp.domain.usecase

import com.example.logifitappp.core.App
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.data.remote.dto.requests.LoginRequest
import com.example.logifitappp.domain.service.AuthService
import com.example.logifitappp.exceptions.HttpConsumerException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authService: AuthService,
    private val updateNotificationToken: UpdateNotificationToken,
    private val updateTenantInformationUseCase: UpdateTenantInformationUseCase
) {
    suspend operator fun invoke(nick: String, password: String): UserModel {
        val user = authService.login(LoginRequest(nick, password))

        App.database.userDao().apply {
            try {
                store(user)
                updateTenantInformationUseCase()
                updateNotificationToken(user.id)
            } catch (e: HttpConsumerException) {
                deleteLoggedIn()
            }
        }

        return user
    }
}