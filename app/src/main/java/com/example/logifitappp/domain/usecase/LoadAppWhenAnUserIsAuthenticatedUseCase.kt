package com.example.logifitappp.domain.usecase

import com.example.logifitappp.core.App
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.data.remote.dto.response.toUser
import com.example.logifitappp.domain.service.UserService
import javax.inject.Inject

class LoadAppWhenAnUserIsAuthenticatedUseCase @Inject constructor(
    private val userService: UserService,
    private val updateNotificationTokenUseCase: UpdateNotificationTokenUseCase,
    private val updateTenantInformationUseCase: UpdateTenantInformationUseCase
) {
    suspend operator fun invoke(accessToken: String): UserModel {
        val user = userService.fetch().toUser(accessToken)

        App.database.userDao().store(user)
        updateTenantInformationUseCase()
        updateNotificationTokenUseCase(user.id)

        return user
    }
}