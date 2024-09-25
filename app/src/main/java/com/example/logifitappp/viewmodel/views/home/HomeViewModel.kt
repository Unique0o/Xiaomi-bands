package com.example.logifitappp.viewmodel.views.home

import com.example.logifitappp.data.models.UserModel

class HomeViewModel {
    val mockUsers = listOf(
        UserModel(
            accessToken = "1",
            firstName = "Mario",
            hasLoggedIn = true,
            id = 1, isActive = true,
            lastName = "Perez Villafranca",
            license = "logifit pro",
            profilePhoto = "profile_photo",
            role = 1,
            tenantId = 1
        )
    )
}