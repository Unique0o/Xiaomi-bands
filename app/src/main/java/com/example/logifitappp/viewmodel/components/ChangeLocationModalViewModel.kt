package com.example.logifitappp.viewmodel.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.core.App
import com.example.logifitappp.data.models.LocationModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = ChangeLocationModalViewModel.ChangeLocationModalViewModelFactory::class)
class ChangeLocationModalViewModel @AssistedInject constructor(
    @Assisted val locationId: Int?
): ViewModel() {
    @AssistedFactory
    interface ChangeLocationModalViewModelFactory {
        fun create(locationId: Int?): ChangeLocationModalViewModel
    }

    var location by mutableStateOf<LocationModel?>(null)

    var locations = mutableStateListOf <LocationModel>()
        private set

    init {
        val user = App.database.userDao().getLoggedIn()!!

        locations.addAll(App.database.locationDao().all(user.tenantId))
        fetchLocation()
    }

    fun fetchLocation() {
        location = locations.find { it.id == locationId }
    }
}