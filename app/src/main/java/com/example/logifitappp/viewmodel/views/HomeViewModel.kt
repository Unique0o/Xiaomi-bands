package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.core.App
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableUpdateSubjectEnum
import com.example.logifitappp.data.models.EvaluationResultModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.domain.usecase.ProcessSynchronizedWearableDataUseCase
import com.example.logifitappp.domain.usecase.SynchronizeWearableUseCase
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.SynchronizationProcessingException
import com.example.logifitappp.viewmodel.states.HomeState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = HomeViewModel.HomeViewModelFactory::class)
class HomeViewModel @AssistedInject constructor(
    @Assisted val user: UserModel,
    private val processSynchronizedWearableDataUseCase: ProcessSynchronizedWearableDataUseCase,
    private val synchronizeWearableUseCase: SynchronizeWearableUseCase
): ViewModel() {
    @AssistedFactory
    interface HomeViewModelFactory {
        fun create(user: UserModel): HomeViewModel
    }

    var evaluations = mutableStateListOf<EvaluationResultModel>()
        private set

    var state by mutableStateOf(HomeState())
        private set

    var wearables = mutableStateListOf<Wearable>()
        private set

    init {
        user.tenantId.let { state = state.copy(tenant = App.database.tenantDao().find(it)) }

        refreshPairedWearables()
        refreshEvaluations()

        user.shiftId?.let { state = state.copy(shift = App.database.shiftDao().find(it)) }
        user.locationId?.let { state = state.copy(location = App.database.locationDao().find(it)) }
    }

    fun checkWearableConnection() {
        refreshPairedWearables()

        if (!state.isLoading) return

        wearables.firstOrNull()?.let { wearable ->
            if (wearable.isInitialized() && wearable.getWearableCoordinator().supportsActivityDataFetching()) fetchActivities(wearable)

            if (wearable.isDisconnected()) {
                state = state.copy(
                    status = if (state.status == AppStatusCodeEnum.CONNECTING_WITH_WEARABLE) AppStatusCodeEnum.FAILED_PASSWORD_RECOVERY
                    else AppStatusCodeEnum.INTERRUPTED_SYNCHRONIZATION
                )
            }
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
            state = state.copy(
                isLoading = true,
                status = AppStatusCodeEnum.EXTRACTING_WEARABLE_INFORMATION
            )

            synchronizeWearableUseCase(state.shift, wearable)
        } catch (e: SynchronizationProcessingException) {
            state = state.copy(status = e.getStatus())
        }
    }

    fun handleAuthenticationKeyFailed() {
       state = state.copy(status = AppStatusCodeEnum.INVALID_WEARABLE_AUTHENTICATION_KEY)
    }

    private fun refreshEvaluations() {
        evaluations.clear()

        if (state.tenant?.shouldItShowDrowsinessTest == true) evaluations.addAll(App.database.evaluationResultDao().fetchFromToday(user.id))
    }

    private fun refreshPairedWearables() {
        wearables.clear()
        wearables.addAll(App.wearableManager.getWearables())

        if (wearables.isNotEmpty()) refreshSleepProcessingData(wearables.first())
    }

    fun refreshSingleWearable(wearable: Wearable) {
        try {
            val index = wearables.indexOf(wearable)

            if (index >= 0) {
                wearables[index].copyFromDevice(wearable)
                processSynchronizedWearableDataUseCase(state.shift, wearable)
                refreshSleepProcessingData(wearable)
            } else refreshPairedWearables()

            state = state.copy(isLoading = false)
        } catch (e: SynchronizationProcessingException) {
            state = state.copy(status = e.getStatus())
        }
    }

    private fun refreshSleepProcessingData(wearable: Wearable) {
        val wearableModel = App.database.wearableDao().find(wearable.getAddress()!!, user.id)!!

        val drowsiness = App.database.drowsinessDao().findFromToday(wearableModel.id)
        val drowsinessCondition = if (drowsiness == null) null else App.database.sleepConditionDao().findAppropriate(drowsiness.totalSleepSeconds, state.tenant!!)

        state = state.copy(
            drowsiness = drowsiness,
            drowsinessCondition = drowsinessCondition,
            fatigue = App.database.fatigueDao().findFromToday(wearableModel.id),
            isSleepSynchronizationRequired = drowsiness == null,
            isSynchronizationWithLogifitRequired = drowsiness?.sentAt == null
        )
    }

    fun stopProcessing() {
        state = state.copy(isLoading = false)
    }
}