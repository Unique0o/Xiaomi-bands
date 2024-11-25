package com.example.logifitappp.viewmodel.components

import androidx.lifecycle.ViewModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.enums.SideBarScreenEnum
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = SideBarViewModel.SideBarViewModelFactory::class)
class SideBarViewModel @AssistedInject constructor(
    @Assisted private val user: UserModel
): ViewModel() {
    @AssistedFactory
    interface SideBarViewModelFactory {
        fun create(user: UserModel): SideBarViewModel
    }

    var options = mutableListOf<SideBarScreenEnum>()
        private set

    init {
        refreshOptions()
    }

    private fun refreshOptions() {
        options.clear()

        if (!user.isAdmin()) {
            options.addAll(listOf(
                SideBarScreenEnum.PERSONAL_INFORMATION,
                SideBarScreenEnum.OCCUPATIONAL_INFORMATION,
                SideBarScreenEnum.HEALTH_INFORMATION,
                SideBarScreenEnum.ROSTER
            ))
        }
    }
}