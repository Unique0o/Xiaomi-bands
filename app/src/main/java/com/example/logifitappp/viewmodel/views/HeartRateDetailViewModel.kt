package com.example.logifitappp.viewmodel.views

import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.core.App
import com.example.logifitappp.core.analyzers.HeartRateAnalyzer
import com.example.logifitappp.core.graphics.HeartRateDataSet
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.domain.usecase.FetchActivityAmountsBetweenDayUseCase
import com.example.logifitappp.viewmodel.states.HeartRateDetailState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = HeartRateDetailViewModel.HeartRateDetailViewModelFactory::class)
    class HeartRateDetailViewModel @AssistedInject constructor(
    @Assisted private val mac: String,
    private val fetchActivityAmountsBetweenDayUseCase: FetchActivityAmountsBetweenDayUseCase
): ViewModel() {
    @AssistedFactory
    interface HeartRateDetailViewModelFactory {
        fun create(mac: String): HeartRateDetailViewModel
    }

    var state by mutableStateOf(HeartRateDetailState())
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

        val activitiesFromToday = fetchActivityAmountsBetweenDayUseCase(state.wearable!!, state.date)
        val heartRateDataSet = HeartRateDataSet(HeartRateAnalyzer().calculate(activitiesFromToday, state.date, 30))

        state = state.copy(
            heartRateDataSet = heartRateDataSet,
            subtitle = if (heartRateDataSet.empty) "N/A" else "${heartRateDataSet.minMeasuredHeartRate} LPM - ${heartRateDataSet.maxMeasuredHeartRate} LPM"
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