package com.example.logifitappp.viewmodel.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.core.App
import com.example.logifitappp.data.models.TenantModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.enums.SideBarScreenEnum
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = SideBarViewModel.SideBarViewModelFactory::class)
class SideBarViewModel @AssistedInject constructor(
    @Assisted private val user: UserModel?
): ViewModel() {
    @AssistedFactory
    interface SideBarViewModelFactory {
        fun create(user: UserModel?): SideBarViewModel
    }

    var options = mutableListOf<SideBarScreenEnum>()
        private set

    var tenant by mutableStateOf<TenantModel?>(null)
        private set

    init {
        tenant = user?.let { App.database.tenantDao().find(it.tenantId) }
        refreshOptions()
    }

    private fun refreshOptions() {
        options.clear()

        if (user?.isAdmin() == false) {
            options.addAll(listOf(
                SideBarScreenEnum.PERSONAL_INFORMATION,
                SideBarScreenEnum.OCCUPATIONAL_INFORMATION,
                SideBarScreenEnum.HEALTH_INFORMATION
            ))

            if (tenant?.shouldItShowRosterOption == true) options.add(SideBarScreenEnum.ROSTER)
        } else options.add(SideBarScreenEnum.WEARABLE_KEY_UPDATE)

        options.addAll(listOf(
            SideBarScreenEnum.TRAININGS,
            SideBarScreenEnum.LOGOUT,
            SideBarScreenEnum.EXIT
        ))
    }
}