package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.core.App
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableUpdateSubjectEnum
import com.example.logifitappp.data.models.LocationModel
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.domain.usecase.ProcessSynchronizedWearableDataUseCase
import com.example.logifitappp.domain.usecase.SynchronizeWearableUseCase
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.SynchronizationProcessingException
import com.example.logifitappp.viewmodel.states.HomeState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel(assistedFactory = HomeViewModel.HomeViewModelFactory::class)
class HomeViewModel @Inject constructor(
    @Assisted val user: UserModel,
    private val processSynchronizedWearableDataUseCase: ProcessSynchronizedWearableDataUseCase,
    private val synchronizeWearableUseCase: SynchronizeWearableUseCase
): ViewModel() {
    @AssistedFactory
    interface HomeViewModelFactory {
        fun create(user: UserModel): HomeViewModel
    }

    var location by mutableStateOf<LocationModel?>(null)
        private set

    var shift by mutableStateOf<ShiftModel?>(null)
        private set

    var state by mutableStateOf(HomeState())
        private set

    var wearables = mutableStateListOf<Wearable>()
        private set

    init {
        refreshPairedWearables()

        user.shiftId?.let { shift = App.database.shiftDao().find(it) }
        user.locationId?.let { location = App.database.locationDao().find(it) }
    }

    fun checkWearableConnection() {
        val wearables = App.wearableManager.getWearables()
        val wearable = wearables.firstOrNull() ?: return

        if (!state.isLoading) return

        if (wearable.isInitialized() && wearable.getWearableCoordinator().supportsActivityDataFetching()) fetchActivities(wearables[0])

        if (wearable.isDisconnected()) {
            state = state.copy(
                status = if (state.status == AppStatusCodeEnum.CONNECTING_WITH_WEARABLE) AppStatusCodeEnum.FAILED_PASSWORD_RECOVERY
                    else AppStatusCodeEnum.INTERRUPTED_SYNCHRONIZATION
            )
        }
    }

    fun connect(wearable: Wearable) {
        state = state.copy(
            isLoading = true,
            status = AppStatusCodeEnum.CONNECTING_WITH_WEARABLE
        )

        if (!wearable.getWearableCoordinator().isConnectable()) {
            wearable.setState(Wearable.State.WAITING_FOR_SCAN)
            wearable.sendDeviceUpdateIntent(App.context, WearableUpdateSubjectEnum.CONNECTION_STATE)

            return
        }

        App.getWearableServiceTo(wearable).connect()
    }

    fun fetchActivities(wearable: Wearable) {
        try {
            state = state.copy(status = AppStatusCodeEnum.EXTRACTING_WEARABLE_INFORMATION)

            synchronizeWearableUseCase(shift, wearable)
        } catch (e: SynchronizationProcessingException) {
            state = state.copy(status = e.getStatus())
        }
    }

    fun handleAuthenticationKeyFailed() {
       state = state.copy(status = AppStatusCodeEnum.INVALID_WEARABLE_AUTHENTICATION_KEY)
    }

    private fun refreshPairedWearables() {
        wearables.clear()
        wearables.addAll(App.wearableManager.getWearables())
    }

    fun refreshSingleWearable(wearable: Wearable) {
        try {
            val index = wearables.indexOf(wearable)

            if (index > 0) {
                wearables[index].copyFromDevice(wearable)
                processSynchronizedWearableDataUseCase(shift, wearable)
            } else refreshPairedWearables()
        } catch (e: SynchronizationProcessingException) {
            state = state.copy(status = e.getStatus())
        }
    }
}