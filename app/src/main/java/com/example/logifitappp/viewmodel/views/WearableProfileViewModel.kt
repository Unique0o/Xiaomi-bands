package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.specs.CallSpec
import com.example.logifitappp.core.utils.AndroidUtils
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.core.utils.DurationUtils
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.enums.CallSpecTypeEnum
import com.example.logifitappp.viewmodel.states.WearableProfileState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.GregorianCalendar

@HiltViewModel(assistedFactory = WearableProfileViewModel.WearableProfileViewModelFactory::class)
class WearableProfileViewModel @AssistedInject constructor(
    @Assisted private val mac: String,
    @Assisted private val user: UserModel?
): ViewModel() {
    @AssistedFactory
    interface WearableProfileViewModelFactory {
        fun create(mac: String, user: UserModel?): WearableProfileViewModel
    }

    var state by mutableStateOf(WearableProfileState())
        private set

    init {
        App.wearableManager.getWearableByMac(mac)?.let {
            state = state.copy(wearable = it)
            refreshSleepProcessingData(it)
        }
    }

    private fun endCallToSmartBand() {
        App.wearableService.onSetCallState(CallSpec(CallSpecTypeEnum.CALL_END))
    }

    fun findSmartBand() {
        state = state.copy(
            isLoading = true,
            status = AppStatusCodeEnum.FINDING_SMART_BAND
        )

        val callSpec = CallSpec(CallSpecTypeEnum.CALL_INCOMING)
        callSpec.number = AndroidUtils.getAppName(App.context)

        App.wearableService.onSetCallState(callSpec)
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

    fun stopProcessing() {
        if (state.status == AppStatusCodeEnum.FINDING_SMART_BAND) endCallToSmartBand()

        state = state.copy(isLoading = false)
    }
}