package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.core.App
import com.example.logifitappp.data.models.UserModel
import java.lang.Thread.sleep

class AppViewModel: ViewModel() {
    var isLoading by mutableStateOf(true)
        private set

    var user by mutableStateOf<UserModel?>(null)
        private set

    init {
        val user = App.database.userDao().getLoggedIn()
        sleep(1500)

        if (user != null) this.user = user

        isLoading = false
    }
}