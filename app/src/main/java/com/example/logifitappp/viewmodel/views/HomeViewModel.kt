package com.example.logifitappp.viewmodel.views

import android.graphics.Bitmap
import android.icu.util.GregorianCalendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.core.utils.SharingUtils
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableSettingPreferenceConstants
import com.example.logifitappp.core.wearebles.WearableUpdateSubjectEnum
import com.example.logifitappp.data.models.EvaluationResultModel
import com.example.logifitappp.data.models.LocationModel
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.data.remote.dto.requests.StoreOccupationalInformationRequest
import com.example.logifitappp.domain.service.UserService
import com.example.logifitappp.domain.usecase.CalculateSleepProcessingUseCase
import com.example.logifitappp.domain.usecase.ProcessSynchronizedWearableDataUseCase
import com.example.logifitappp.domain.usecase.SendWearableInformationToLogifitUseCase
import com.example.logifitappp.domain.usecase.ShareEvaluationDetailUseCase
import com.example.logifitappp.domain.usecase.SynchronizeWearableUseCase
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.example.logifitappp.exceptions.SynchronizationProcessingException
import com.example.logifitappp.viewmodel.states.HomeState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException

@HiltViewModel(assistedFactory = HomeViewModel.HomeViewModelFactory::class)
class HomeViewModel @AssistedInject constructor(
    @Assisted val user: UserModel,
    private val calculateSleepProcessingUseCase: CalculateSleepProcessingUseCase,
    private val processSynchronizedWearableDataUseCase: ProcessSynchronizedWearableDataUseCase,
    private val sendWearableInformationToLogifitUseCase: SendWearableInformationToLogifitUseCase,
    private val shareEvaluationDetailUseCase: ShareEvaluationDetailUseCase,
    private val synchronizeWearableUseCase: SynchronizeWearableUseCase,
    private val userService: UserService
): ViewModel() {
    @AssistedFactory
    interface HomeViewModelFactory {
        fun create(user: UserModel): HomeViewModel
    }

    var bitmap by mutableStateOf<Bitmap?>(null)

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

    private fun fetchActivities(wearable: Wearable) {
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

    fun handleChangeShift(shift: ShiftModel) {
        viewModelScope.launch {
            try {
                App.database.userDao().store(user.copy(shiftId = shift.id))
                state = state.copy(shift = shift)

                wearables.firstOrNull()?.let {
                    calculateSleepProcessingUseCase(shift, it)
                    refreshSleepProcessingData(it)
                }

                App.signalReloadAuthenticatedUser()

                userService.storeOccupationalInformation(user.id, StoreOccupationalInformationRequest(shift_id = shift.id))
            } catch (e: Exception) {
                //TODO: require update user occupational information
            }
        }
    }

    fun handleChangeLocation(location: LocationModel) {
        viewModelScope.launch {
            try {
                App.database.userDao().store(user.copy(locationId = location.id))
                state = state.copy(location = location)

                App.signalReloadAuthenticatedUser()

                userService.storeOccupationalInformation(user.id, StoreOccupationalInformationRequest(location_aux_id = location.id))
            } catch (e: Exception) {
                //TODO: require update user occupational information
            }
        }
    }

    private fun refreshEvaluations() {
        evaluations.clear()

        if (state.tenant?.shouldItShowDrowsinessTest == true) evaluations.addAll(App.database.evaluationResultDao().fetchFromToday(user.id))
    }

    private fun refreshPairedWearables() {
        wearables.clear()
        wearables.addAll(App.wearableManager.getWearables())
        wearables.firstOrNull()?.let { refreshSleepProcessingData(it) }
    }

    fun refreshSingleWearable(wearable: Wearable) {
        try {
            val index = wearables.indexOf(wearable)

            if (index >= 0) {
                wearables[index].copyFromDevice(wearable)
                processSynchronizedWearableDataUseCase(state.shift, wearable)
                refreshSleepProcessingData(wearable)

                if (App.getWearablePreferences(wearable.getAddress()!!).getSendInformationWhenConnect()) {
                    sendSleep(wearable)
                    return
                }
            } else refreshPairedWearables()

            state = state.copy(status = AppStatusCodeEnum.SUCCESSFUL_WEARABLE_INFORMATION_SYNCHRONIZING)
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

    fun reportSleep(wearable: Wearable) {
        val preferences = App.getWearablePreferences(wearable.getAddress()!!)

        if (preferences.getFirstConnection()) {
            fetchActivities(wearable)

            preferences.getPreferences()
                .edit()
                .remove(WearableSettingPreferenceConstants.PREF_FIRST_CONNECTION)
                .apply()
        } else sendSleep(wearable)
    }

    private fun sendSleep(wearable: Wearable) {
        viewModelScope.launch {
            try {
                state = state.copy(
                    isLoading = true,
                    status = AppStatusCodeEnum.TRANSFERRING_WEARABLE_INFORMATION
                )

                sendWearableInformationToLogifitUseCase(state.shift, wearable)
                refreshSleepProcessingData(wearable)

                state = state.copy(status = AppStatusCodeEnum.SUCCESSFUL_WEARABLE_INFORMATION_TRANSFERRING)
            } catch (e: SynchronizationProcessingException) {
                state = state.copy(status = e.getStatus())
            } catch (e: Exception) {
                state = state.copy(status = AppStatusCodeEnum.UNPROCESSABLE_WEARABLE_INFORMATION_TRANSFER)
            }
        }
    }

    fun shareEvaluation(evaluation: EvaluationResultModel) {
        viewModelScope.launch {
            try {
                state = state.copy(
                    isLoading = true,
                    status = AppStatusCodeEnum.DOWNLOADING_EVALUATION_RESULT
                )

                shareEvaluationDetailUseCase(App.context, evaluation)
                state = state.copy(isLoading = false)
            } catch (e: HttpConsumerException) {
                state = state.copy(status = e.getStatus())
            }
        }
    }

    fun shareSleepDetail() {
        try {
            bitmap?.let {
                val filename = "sleep_detail_${DateTimeUtils.format(GregorianCalendar.getInstance().time, "yyyy_MM_dd_HH_mm_ss")}.png"

                SharingUtils.share(App.context, it, filename, R.string.share_sleep_detail_message)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun stopProcessing() {
        state = state.copy(isLoading = false)
    }
}