package com.example.logifitappp.viewmodel.views

import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.analyzers.StepsAnalyzer
import com.example.logifitappp.core.graphics.StepsBarDataSet
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.domain.usecase.FetchActivityAmountsBetweenDayUseCase
import com.example.logifitappp.viewmodel.states.StepsDetailState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = StepsDetailViewModel.StepsDetailViewModelFactory::class)
class StepsDetailViewModel @AssistedInject constructor(
    @Assisted private val mac: String,
    private val fetchActivityAmountsBetweenDayUseCase: FetchActivityAmountsBetweenDayUseCase
): ViewModel() {
    @AssistedFactory
    interface StepsDetailViewModelFactory {
        fun create(mac: String): StepsDetailViewModel
    }

    var state by mutableStateOf(StepsDetailState())
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
        val stepsDataSet = StepsBarDataSet(StepsAnalyzer().calculate(activitiesFromToday, state.date, 30))

        state = state.copy(
            stepsDataSet = stepsDataSet,
            subtitle = if (stepsDataSet.empty) "N/A" else App.context.getString(R.string.steps_detail_subtitle, stepsDataSet.totalSteps.toString())
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