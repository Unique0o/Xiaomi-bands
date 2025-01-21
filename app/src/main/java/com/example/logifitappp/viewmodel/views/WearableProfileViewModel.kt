package com.example.logifitappp.viewmodel.views

import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.specs.CallSpec
import com.example.logifitappp.core.utils.AndroidUtils
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.core.utils.DurationUtils
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.data.remote.dto.requests.AssociateWearableRequest
import com.example.logifitappp.domain.service.WearableService
import com.example.logifitappp.domain.usecase.ProcessSynchronizedWearableDataUseCase
import com.example.logifitappp.domain.usecase.SendWearableInformationToLogifitUseCase
import com.example.logifitappp.domain.usecase.SynchronizeWearableUseCase
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.enums.CallSpecTypeEnum
import com.example.logifitappp.enums.WearableUpdateSubjectEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.example.logifitappp.exceptions.SynchronizationProcessingException
import com.example.logifitappp.viewmodel.states.WearableProfileState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.GregorianCalendar

@HiltViewModel(assistedFactory = WearableProfileViewModel.WearableProfileViewModelFactory::class)
class WearableProfileViewModel @AssistedInject constructor(
    @Assisted private val mac: String,
    @Assisted private val user: UserModel?,
    private val processSynchronizedWearableDataUseCase: ProcessSynchronizedWearableDataUseCase,
    private val sendWearableInformationToLogifitUseCase: SendWearableInformationToLogifitUseCase,
    private val synchronizeWearableUseCase: SynchronizeWearableUseCase,
    private val wearableService: WearableService
): ViewModel() {
    @AssistedFactory
    interface WearableProfileViewModelFactory {
        fun create(mac: String, user: UserModel?): WearableProfileViewModel
    }

    var state by mutableStateOf(WearableProfileState())
        private set

    init {
        user?.shiftId?.let { state = state.copy(shift = App.database.shiftDao().find(it, user.tenantId)) }
        refreshWearable()
    }

    fun checkWearableConnection() {
        if (user == null) return

        refreshWearable()

        viewModelScope.launch {
            state.wearable?.let { wearable ->
                try {
                    if (!user.isAdmin()) {
                        wearableService.associate(user.id, AssociateWearableRequest(
                            device_mac = wearable.getAddress()!!,
                            oper_system = "Android",
                            oper_system_version = Build.VERSION.RELEASE,
                            phone_brand = Build.BRAND,
                            phone_model = Build.MODEL
                        ))
                    }
                } catch (_: Exception) {

                }

                if (!state.isLoading) return@launch

                if (wearable.isInitialized() && wearable.getWearableCoordinator().supportsActivityDataFetching() && state.status == AppStatusCodeEnum.CONNECTING_WITH_WEARABLE) {
                    state = state.copy(isLoading = false)
                }

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

    private fun endCallToSmartBand() {
        App.getWearableServiceTo(state.wearable!!).onSetCallState(CallSpec(CallSpecTypeEnum.CALL_END))
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

    fun findSmartBand() {
        state = state.copy(
            isLoading = true,
            status = AppStatusCodeEnum.FINDING_SMART_BAND
        )

        val callSpec = CallSpec(CallSpecTypeEnum.CALL_INCOMING)
        callSpec.number = AndroidUtils.getAppName(App.context)

        App.getWearableServiceTo(state.wearable!!).onSetCallState(callSpec)
    }

    fun handleFailedConnection(status: AppStatusCodeEnum) {
        state = state.copy(status = status)
    }

    private fun refreshSleepProcessingData(wearable: Wearable) {
        if (user == null) return

        val wearableModel = App.database.wearableDao().find(wearable.getAddress()!!, user.id)!!
        val drowsiness = App.database.drowsinessDao().findFromToday(wearableModel.id)

        when {
            drowsiness == null -> {
                state = state.copy(
                    activityDataSynchronizationMessage = App.context.getString(R.string.lack_of_sleep_synchronization_message),
                    synchronizationWithLogifitMessage = App.context.getString(R.string.lack_of_sleep_synchronization_with_logifit_message),
                    totalSleepTimeMessage = App.context.getString(R.string.lack_of_sleep_synchronization_message)
                )
            }

            else -> {
                val now = GregorianCalendar.getInstance().timeInMillis / 1000
                val createAtInMillis = DateTimeUtils.parse(drowsiness.createdAt, "yyyy-MM-dd HH:mm:ss")!!.time / 1000
                val sentAtInMillis = if (drowsiness.sentAt == null) 0L else DateTimeUtils.parse(drowsiness.sentAt, "yyyy-MM-dd HH:mm:ss")!!.time / 1000

                state = state.copy(
                    activityDataSynchronizationMessage = App.context.getString(
                        R.string.sleep_synchronization_message,
                        drowsiness.createdAt,
                        DurationUtils.formatExtended(App.context, now - createAtInMillis)
                    ),

                    synchronizationWithLogifitMessage = if (drowsiness.sentAt == null) App.context.getString(R.string.lack_of_sleep_synchronization_with_logifit_message)
                    else App.context.getString(
                        R.string.sleep_synchronization_message,
                        drowsiness.sentAt,
                        DurationUtils.formatExtended(App.context, now - sentAtInMillis)
                    ),

                    totalSleepTimeMessage = if (drowsiness.totalSleepSeconds == 0L) App.context.getString(R.string.lack_of_sleep_synchronization_message)
                    else App.context.getString(R.string.sleep_hours_message, DurationUtils.format(drowsiness.totalSleepSeconds))
                )
            }
        }
    }

    private fun refreshWearable() {
        App.wearableManager.getWearableByMac(mac)?.let {
            if (user?.isAdmin() == true) {
                val wearable = App.database.wearableDao().find(it.getAddress()!!, user.id)!!
                state = state.copy(shift = wearable.shiftId?.let { id -> App.database.shiftDao().find(id, user.tenantId) })
            }

            state = state.copy(wearable = it)
            refreshSleepProcessingData(it)
        }
    }

    fun refreshSingleWearable(wearable: Wearable) {
        try {
            if (wearable.getAddress() == state.wearable?.getAddress()) {
                state = state.copy(wearable = wearable)
                processSynchronizedWearableDataUseCase(state.shift, wearable)
                refreshSleepProcessingData(wearable)
            }

            state = state.copy(status = AppStatusCodeEnum.SUCCESSFUL_WEARABLE_INFORMATION_SYNCHRONIZING)
        } catch (e: SynchronizationProcessingException) {
            state = state.copy(status = e.getStatus())
        }
    }

    fun sendSleep(wearable: Wearable) {
        viewModelScope.launch {
            try {
                state = state.copy(
                    isBandTheft = false,
                    isLoading = true,
                    status = AppStatusCodeEnum.TRANSFERRING_WEARABLE_INFORMATION
                )

                sendWearableInformationToLogifitUseCase(state.shift, wearable)
                refreshSleepProcessingData(wearable)

                state = state.copy(status = AppStatusCodeEnum.SUCCESSFUL_WEARABLE_INFORMATION_TRANSFERRING)
            } catch (e: SynchronizationProcessingException) {
                state = state.copy(status = e.getStatus())
            } catch (e: HttpConsumerException) {
                state = state.copy(
                    isBandTheft = e.getStatus() == AppStatusCodeEnum.BAND_THEFT,
                    status = e.getStatus()
                )
            } catch (e: Exception) {
                state = state.copy(status = AppStatusCodeEnum.UNPROCESSABLE_WEARABLE_INFORMATION_TRANSFER)
            }
        }
    }

    fun stopProcessing() {
        if (state.status == AppStatusCodeEnum.FINDING_SMART_BAND) endCallToSmartBand()

        state = state.copy(isLoading = false)
    }
}