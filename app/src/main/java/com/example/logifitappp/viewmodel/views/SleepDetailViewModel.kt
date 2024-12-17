package com.example.logifitappp.viewmodel.views

import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.graphics.SleepBarDataSet
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.core.utils.DurationUtils
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.domain.usecase.FetchActivityAmountsByShiftUseCase
import com.example.logifitappp.viewmodel.states.SleepDetailState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.ZoneId

@HiltViewModel(assistedFactory = SleepDetailViewModel.SleepDetailViewModelFactory::class)
class SleepDetailViewModel @AssistedInject constructor(
    @Assisted private val mac: String,
    @Assisted private val user: UserModel?,
    private val fetchActivityAmountsByShiftUseCase: FetchActivityAmountsByShiftUseCase
): ViewModel() {
    @AssistedFactory
    interface SleepDetailViewModelFactory {
        fun create(mac: String, user: UserModel?): SleepDetailViewModel
    }

    var state by mutableStateOf(SleepDetailState())
        private set

    init {
        user?.shiftId?.let {
            val shift = App.database.shiftDao().find(it, user.tenantId)

            state = state.copy(
                shift = shift,
                summary = App.context.getString(R.string.sleep_detail_summary, shift?.startTime ?: "N/A", shift?.endTime ?: "N/A")
            )
        }

        state = state.copy(wearable = App.wearableManager.getWearableByMac(mac))

        fetchInformation()
    }

    private fun fetchInformation() {
        if (state.shift == null || state.wearable == null) return

        val dataset = SleepBarDataSet(fetchActivityAmountsByShiftUseCase(state.shift!!, state.wearable!!, state.date))

        state = state.copy(
            sleepDataSet = dataset,
            subtitle = DurationUtils.format(dataset.amounts.totalSleepMinutes * 60)
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
            canGoToNextDay = DateTimeUtils.format(calendar.time, "dd/MM/yyyy") != DateTimeUtils.format(Calendar.getInstance().time, "dd/MM/yyyy"),
            date = calendar
        )

        fetchInformation()
    }

    fun nextDay() {
        if (!state.canGoToNextDay) return

        val calendar = state.date.clone() as Calendar
        calendar.add(Calendar.DAY_OF_MONTH, 1)

        state = state.copy(
            canGoToNextDay = DateTimeUtils.format(calendar.time, "dd/MM/yyyy") != DateTimeUtils.format(Calendar.getInstance().time, "dd/MM/yyyy"),
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