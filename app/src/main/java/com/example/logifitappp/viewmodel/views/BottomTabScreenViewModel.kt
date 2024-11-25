package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.core.App
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.enums.BottomTabScreenEnum
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = BottomTabScreenViewModel.BottomTabScreenViewModelFactory::class)
class BottomTabScreenViewModel @AssistedInject constructor(
    @Assisted private val user: UserModel
): ViewModel() {
    @AssistedFactory
    interface BottomTabScreenViewModelFactory {
        fun create(user: UserModel): BottomTabScreenViewModel
    }

    var index by mutableIntStateOf(0)

    var screens = mutableStateListOf<BottomTabScreenEnum>()
        private set

    var wearables = mutableStateListOf<Wearable>()
        private set

    init {
        refreshPairedWearables()
    }

    fun refreshPairedWearables() {
        wearables.clear()
        wearables.addAll(App.wearableManager.getWearables())

        refreshScreens()
    }

    private fun refreshScreens() {
        screens.clear()

        when {
            user.isAdmin() -> {

            }

            else -> {
                screens.add(BottomTabScreenEnum.HOME)

                wearables.firstOrNull()?.let {
                    screens.add(BottomTabScreenEnum.GRAPHICS)
                }
            }
        }
    }
}