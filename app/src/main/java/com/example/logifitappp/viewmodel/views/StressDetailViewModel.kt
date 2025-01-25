package com.example.logifitappp.viewmodel.views

import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.core.App
import com.example.logifitappp.core.graphics.StressDataSet
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.domain.usecase.FetchStressSampleAmountBetweenDayUseCase
import com.example.logifitappp.viewmodel.states.StressDetailState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = StressDetailViewModel.StressDetailViewModelFactory::class)
class StressDetailViewModel @AssistedInject constructor(
    @Assisted private val mac: String,
    private val fetchStressSampleAmountBetweenDayUseCase: FetchStressSampleAmountBetweenDayUseCase
): ViewModel() {
    @AssistedFactory
    interface StressDetailViewModelFactory {
        fun create(mac: String): StressDetailViewModel
    }

    var state by mutableStateOf(StressDetailState())
        private set

    init {
        state = state.copy(wearable = App.wearableManager.getWearableByMac(mac))

        fetchInformation()
    }

    private fun checkIfCanGoToNext(calendar: Calendar): Boolean {
        return DateTimeUtils.format(calendar.time, "dd/MM/yyyy") != DateTimeUtils.format(Calendar.getInstance().time, "dd/MM/yyyy")
    }

    private fun fetchInformation() {
        if (state.wearable == null) return

        val spo2DataSet = StressDataSet(fetchStressSampleAmountBetweenDayUseCase(state.wearable!!, state.date))

        state = state.copy(
            stressDataSet = spo2DataSet,
            subtitle = if (spo2DataSet.empty) "N/A" else "${spo2DataSet.minMeasuredStress} - ${spo2DataSet.maxMeasuredStress}"
        )
    }

    fun handleChangeDateInMillis(timeInMillis: Long?) {
        val calendar = Calendar.getInstance()

        timeInMillis?.let { calendar.timeInMillis = it }

        state = state.copy(
            canGoToNextDay = checkIfCanGoToNext(calendar),
            date = calendar
        )

        fetchInformation()
    }

    fun nextDay() {
        if (!state.canGoToNextDay) return

        val calendar = state.date.clone() as Calendar
        calendar.add(Calendar.DAY_OF_MONTH, 1)

        state = state.copy(
            canGoToNextDay = checkIfCanGoToNext(calendar),
            date = calendar
        )

        fetchInformation()
    }

    fun prevDay() {
        val calendar = state.date.clone() as Calendar
        calendar.add(Calendar.DAY_OF_MONTH, -1)

        state = state.copy(
            canGoToNextDay = true,
            date = calendar
        )

        fetchInformation()
    }
}