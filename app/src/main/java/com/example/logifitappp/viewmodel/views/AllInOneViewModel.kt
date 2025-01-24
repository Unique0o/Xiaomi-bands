package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.logifitappp.core.App
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.DrowsinessModel
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.data.models.SleepConditionModel
import com.example.logifitappp.data.models.TenantModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.domain.service.AdminService
import com.example.logifitappp.domain.usecase.FindAppropriateSleepConditionUseCase
import com.example.logifitappp.domain.usecase.ProcessSynchronizedWearableDataUseCase
import com.example.logifitappp.domain.usecase.SendWearableInformationToLogifitUseCase
import com.example.logifitappp.domain.usecase.SynchronizeWearableUseCase
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.enums.WearableUpdateSubjectEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.example.logifitappp.exceptions.SynchronizationProcessingException
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.viewmodel.states.AllInOneState
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AllInOneViewModel.AllInOneViewModelFactory::class)
class AllInOneViewModel @AssistedInject constructor(
    @Assisted private val navigation: NavHostController,
    @Assisted private val tenant: TenantModel?,
    @Assisted private val user: UserModel?,
    private val adminService: AdminService,
    private val findAppropriateSleepConditionUseCase: FindAppropriateSleepConditionUseCase,
    private val processSynchronizedWearableDataUseCase: ProcessSynchronizedWearableDataUseCase,
    private val sendWearableInformationToLogifitUseCase: SendWearableInformationToLogifitUseCase,
    private val synchronizeWearableUseCase: SynchronizeWearableUseCase
): ViewModel() {
    @AssistedFactory
    interface AllInOneViewModelFactory {
        fun create(navigation: NavHostController, user: UserModel?, tenant: TenantModel?): AllInOneViewModel
    }

    var state by mutableStateOf(AllInOneState())
        private set

    val filteredWearables = mutableStateListOf<Wearable>()
    private val wearables = mutableStateListOf<Wearable>()

    init {
        App.refreshWearables()
        refreshPairedWearables()
    }

    fun checkWearableConnection() {
        viewModelScope.launch {
            refreshPairedWearables()

            state.currentWearable?.let { currentWearable ->
                val wearable = wearables.find { it.getAddress() == currentWearable.getAddress() }

                if (wearable == null) return@launch

                if (!state.isLoading) return@launch

                if (wearable.isInitialized() && wearable.getWearableCoordinator().supportsActivityDataFetching() && state.status == AppStatusCodeEnum.CONNECTING_WITH_WEARABLE) fetchActivities(wearable)

                if (wearable.isDisconnected()) {
                    val status = when (state.status) {
                        AppStatusCodeEnum.CONNECTING_WITH_WEARABLE -> AppStatusCodeEnum.FAILED_WEARABLE_PAIRING
                        AppStatusCodeEnum.EXTRACTING_WEARABLE_INFORMATION -> AppStatusCodeEnum.INTERRUPTED_SYNCHRONIZATION
                        else -> state.status
                    }

                    state = state.copy(status = status)
                }
            }
        }
    }

    fun closeUnpairWearableModal() {
        state = state.copy(
            currentWearable = null,
            shouldShowUnpairWearableModal = false
        )
    }

    fun closeWearableShiftModal() {
        state = state.copy(
            currentShift = null,
            currentWearable = null,
            shouldShowWearableShiftModal = false
        )
    }

    fun connect(wearable: Wearable) {
        state = state.copy(
            currentWearable = wearable,
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

            synchronizeWearableUseCase(fetchShift(wearable), wearable)
        } catch (e: SynchronizationProcessingException) {
            state = state.copy(status = e.getStatus())
        }
    }

    fun fetchDrowsiness(wearable: Wearable): DrowsinessModel? {
        if (user == null) return null

        val wearableModel = App.database.wearableDao().find(wearable.getAddress()!!, user.id)!!

        return App.database.drowsinessDao().findFromToday(wearableModel.id)
    }

    fun fetchSleepCondition(drowsiness: DrowsinessModel?): SleepConditionModel? {
        if (drowsiness == null) return null

        if (tenant == null) return null

        return findAppropriateSleepConditionUseCase(drowsiness.totalSleepSeconds, tenant)
    }

    fun fetchShift(wearable: Wearable): ShiftModel? {
        if (user == null) return null

        if (tenant == null) return null

        val wearableModel = App.database.wearableDao().find(wearable.getAddress()!!, user.id)!!

        return wearableModel.shiftId?.let { App.database.shiftDao().find(it, tenant.id) }
    }

    fun goToPairingWorkerPage(wearable: Wearable) {
        viewModelScope.launch {
            try {
                state = state.copy(
                    isLoading = true,
                    status = AppStatusCodeEnum.FETCHING_WORKER_LIST
                )

                val workers = adminService.fetchWorkers()
                val gson = Gson()

                state = state.copy(isLoading = false)

                navigation.navigate(MainRoutes.PairingWorker(
                    wearable.getAddress()!!,
                    gson.toJson(workers.toTypedArray())
                ))
            } catch (e: HttpConsumerException) {
                state = state.copy(status = e.getStatus())
            }
        }
    }

    fun handleSelectShift(shift: ShiftModel) {
        if (user == null) return

        if (state.currentWearable == null) return

        val wearableModel = App.database.wearableDao().find(state.currentWearable!!.getAddress()!!, user.id)!!
        wearableModel.shiftId = shift.id

        App.database.wearableDao().store(wearableModel)
    }

    fun handleUnpairWearable(wearable: Wearable) {
        val newFilteredWearables = filteredWearables.filter { it.getAddress() != wearable.getAddress() }
        filteredWearables.clear()
        filteredWearables.addAll(newFilteredWearables)

        val newWearables = wearables.filter { it.getAddress() != wearable.getAddress() }
        wearables.clear()
        wearables.addAll(newWearables)
    }

    fun openUnpairWearableModal(wearable: Wearable) {
        state = state.copy(
            currentWearable = wearable,
            shouldShowUnpairWearableModal = true
        )
    }

    fun openWearableShiftModal(wearable: Wearable) {
        state = state.copy(
            currentShift = fetchShift(wearable),
            currentWearable = wearable,
            shouldShowWearableShiftModal = true
        )
    }

    private fun refreshPairedWearables() {
        wearables.clear()
        filteredWearables.clear()

        App.wearableManager.getWearables().let {
            wearables.addAll(it)
            filteredWearables.addAll(it)
        }
    }

    fun refreshSingleWearable(wearable: Wearable) {
        try {
            val index = wearables.indexOf(wearable)

            if (index >= 0) {
                wearables[index].copyFromDevice(wearable)
                processSynchronizedWearableDataUseCase(fetchShift(wearable), wearable)
                state = state.copy(currentWearable = wearable)

                sendSleep(wearable)
            } else {
                refreshPairedWearables()
                state = state.copy(status = AppStatusCodeEnum.SUCCESSFUL_WEARABLE_INFORMATION_SYNCHRONIZING)
            }
        } catch (e: SynchronizationProcessingException) {
            state = state.copy(status = e.getStatus())
        }
    }

    fun search(value: TextFieldValue) {
        state = state.copy(searchText = value)
        filteredWearables.clear()

        if (value.text.isBlank()) filteredWearables.addAll(wearables)
        else filteredWearables.addAll(wearables.filter { it.getAliasOrName().contains(value.text, true) or it.getAddress()!!.contains(value.text, true) })
    }

    private fun sendSleep(wearable: Wearable) {
        viewModelScope.launch {
            try {
                state = state.copy(
                    isLoading = true,
                    status = AppStatusCodeEnum.TRANSFERRING_WEARABLE_INFORMATION
                )

                sendWearableInformationToLogifitUseCase(fetchShift(wearable), wearable)
                state = state.copy(status = AppStatusCodeEnum.SUCCESSFUL_WEARABLE_INFORMATION_TRANSFERRING)
            } catch (e: SynchronizationProcessingException) {
                state = state.copy(status = e.getStatus())
            } catch (e: HttpConsumerException) {
                state = state.copy(status = e.getStatus())
            } catch (e: Exception) {
                state = state.copy(status = AppStatusCodeEnum.UNPROCESSABLE_WEARABLE_INFORMATION_TRANSFER)
            }
        }
    }

    fun stopFetchingWorkersProcessing() {
        state = state.copy(isLoading = false)
    }
}