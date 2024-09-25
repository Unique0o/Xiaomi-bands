package com.example.logifitappp.viewmodel.views.graphics

import com.example.logifitappp.data.models.UserModel

class GraphicsViewModel {

    val mockUsers = listOf(
        UserModel(
            accessToken = "1",
            firstName = "Mario",
            hasLoggedIn = true,
            id = 1, isActive = true,
            lastName = "Perez Villafranca",
            license = "logifit premium",
            profilePhoto = "profile_photo",
            role = 1,
            tenantId = 1
        )
    )
}