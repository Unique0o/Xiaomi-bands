package com.example.logifitappp.viewmodel.views

import android.icu.util.GregorianCalendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.core.App
import com.example.logifitappp.core.analyzers.HeartRateAmountList
import com.example.logifitappp.core.analyzers.HeartRateAnalyzer
import com.example.logifitappp.core.analyzers.StepsAmountList
import com.example.logifitappp.core.analyzers.StepsAnalyzer
import com.example.logifitappp.core.graphics.HeartRateDataSet
import com.example.logifitappp.core.graphics.SleepDataSet
import com.example.logifitappp.core.graphics.Spo2DataSet
import com.example.logifitappp.core.graphics.StepsDataSet
import com.example.logifitappp.core.graphics.StressDataSet
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.domain.usecase.FetchActivityAmountsBetweenDayUseCase
import com.example.logifitappp.domain.usecase.FetchActivityAmountsByShiftUseCase
import com.example.logifitappp.domain.usecase.FetchSpo2SampleAmountsBetweenDayUseCase
import com.example.logifitappp.domain.usecase.FetchStressSampleAmountBetweenDayUseCase
import com.example.logifitappp.domain.usecase.SendWearableInformationToLogifitUseCase
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.SynchronizationProcessingException
import com.example.logifitappp.viewmodel.states.GraphicsState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GraphicsViewModel.GraphicsViewModelFactory::class)
class GraphicsViewModel @AssistedInject constructor(
    @Assisted private val mac: String?,
    @Assisted private val user: UserModel?,
    private val fetchActivityAmountsBetweenDayUseCase: FetchActivityAmountsBetweenDayUseCase,
    private val fetchActivityAmountsByShiftUseCase: FetchActivityAmountsByShiftUseCase,
    private val fetchSpo2SampleAmountsBetweenDayUseCase: FetchSpo2SampleAmountsBetweenDayUseCase,
    private val fetchStressSampleAmountBetweenDayUseCase: FetchStressSampleAmountBetweenDayUseCase,
    private val sendWearableInformationToLogifitUseCase: SendWearableInformationToLogifitUseCase
): ViewModel() {
    @AssistedFactory
    interface GraphicsViewModelFactory {
        fun create(mac: String?, user: UserModel?): GraphicsViewModel
    }

    var state by mutableStateOf(GraphicsState())
        private set

    init {
        user?.shiftId?.let {
            val wearable = if (mac == null) App.wearableManager.getWearables().first() else App.wearableManager.getWearableByMac(mac)
            val shift = App.database.shiftDao().find(it, user.tenantId)

            refreshGraphics(shift, wearable)
        }
    }

    private fun calculate(shift: ShiftModel?, wearable: Wearable) {
        val calendar = GregorianCalendar.getInstance()
        val activitiesFromToday = fetchActivityAmountsBetweenDayUseCase(wearable, calendar)

        val heartRateAnalyzer = HeartRateDataSet(when (wearable.getWearableCoordinator().supportsHeartRateMeasurement()) {
            true -> HeartRateAnalyzer().calculate(activitiesFromToday, calendar, 30)
            else -> HeartRateAmountList()
        })

        val stepsDataset = StepsDataSet(when (wearable.getWearableCoordinator().supportsActivityTracking()) {
            true -> StepsAnalyzer().calculate(activitiesFromToday, calendar, 30)
            else -> StepsAmountList()
        })

        state = state.copy(
            heartRateDataSet = heartRateAnalyzer,
            shift = shift,
            spo2DataSet = Spo2DataSet(fetchSpo2SampleAmountsBetweenDayUseCase(wearable, calendar)),
            stepsDataset = stepsDataset,
            stressDataSet = StressDataSet(fetchStressSampleAmountBetweenDayUseCase(wearable, calendar))
        )

        if (shift == null) return

        state = state.copy(sleepDataSet = SleepDataSet(fetchActivityAmountsByShiftUseCase(shift, wearable)))
    }

    fun refreshGraphics(shift: ShiftModel?, wearable: Wearable?) {
        if (wearable == null) return

        calculate(shift, wearable)
        refreshSleepProcessingData(wearable)
    }

    private fun refreshSleepProcessingData(wearable: Wearable) {
        if (user == null) return

        val wearableModel = App.database.wearableDao().find(wearable.getAddress()!!, user.id)!!

        state = state.copy(
            drowsiness = App.database.drowsinessDao().findFromToday(wearableModel.id),
            wearable = wearable
        )
    }

    fun sendSleep(wearable: Wearable) {
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

    fun stopProcessing() {
        state = state.copy(isLoading = false)
    }
}