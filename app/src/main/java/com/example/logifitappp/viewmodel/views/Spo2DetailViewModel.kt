package com.example.logifitappp.viewmodel.views

import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.core.App
import com.example.logifitappp.core.graphics.Spo2DataSet
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.domain.usecase.FetchSpo2SampleAmountsBetweenDayUseCase
import com.example.logifitappp.viewmodel.states.Spo2DetailState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.ZoneId

@HiltViewModel(assistedFactory = Spo2DetailViewModel.Spo2DetailViewModelFactory::class)
class Spo2DetailViewModel @AssistedInject constructor(
    @Assisted private val mac: String,
    private val fetchSpo2SampleAmountsBetweenDayUseCase: FetchSpo2SampleAmountsBetweenDayUseCase
): ViewModel() {
    @AssistedFactory
    interface Spo2DetailViewModelFactory {
        fun create(mac: String): Spo2DetailViewModel
    }

    var state by mutableStateOf(Spo2DetailState())
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

        val spo2DataSet = Spo2DataSet(fetchSpo2SampleAmountsBetweenDayUseCase(state.wearable!!, state.date))

        state = state.copy(
            spo2DataSet = spo2DataSet,
            subtitle = if (spo2DataSet.empty) "-" else "${spo2DataSet.minMeasuredSpo2}% - ${spo2DataSet.maxMeasuredSpo2}%"
        )
    }

    fun handleChangeDateInMillis(timeInMillis: Long?) {
        val calendar = Calendar.getInstance()

        if (timeInMillis != null) {
            val instant = Instant.ofEpochMilli(timeInMillis)
            val systemZone = ZoneId.systemDefault()
            val zonedDateTime = instant.atZone(systemZone)

            /*val timeZone = TimeZone.getDefault()
            val offset = timeZone.getOffset(timeInMillis)*/

            calendar.timeInMillis = zonedDateTime.toInstant().toEpochMilli()
        }

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