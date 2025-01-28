package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.core.App
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.viewmodel.states.WearableSettingsState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = WearableSettingsViewModel.WearableSettingsViewModelFactory::class)
class WearableSettingsViewModel @AssistedInject constructor(
    @Assisted private val navigation: NavHostController,
    @Assisted private val mac: String
): ViewModel() {
    @AssistedFactory
    interface WearableSettingsViewModelFactory {
        fun create(navigation: NavHostController, mac: String): WearableSettingsViewModel
    }

    var state by mutableStateOf(WearableSettingsState())
        private set

    init {
        state = state.copy(wearable = App.wearableManager.getWearableByMac(mac))
    }

    fun closeUnpairWearableModal() {
        state = state.copy(shouldShowUnpairWearableModal = false)
    }

    fun handleUnpairWearable(wearable: Wearable) {
        navigation.navigate(MainRoutes.SplashScreen)
        App.wearableManager.refreshPairedWearables()
    }

    fun openUnpairWearableModal() {
        state = state.copy(shouldShowUnpairWearableModal = true)
    }
}