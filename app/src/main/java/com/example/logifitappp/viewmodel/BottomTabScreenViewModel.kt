package com.example.logifitappp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.enums.BottomTabScreenEnum

class BottomTabScreenViewModel: ViewModel() {
    var index by mutableIntStateOf(0)

    fun getScreens(user: UserModel): List<BottomTabScreenEnum> {
        val screens = mutableListOf<BottomTabScreenEnum>()

        screens.add(BottomTabScreenEnum.HOME)
        screens.add(BottomTabScreenEnum.GRAPHICS)

        return screens
    }
}