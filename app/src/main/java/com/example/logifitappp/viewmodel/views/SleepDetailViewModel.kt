package com.example.logifitappp.viewmodel.views

import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.graphics.SleepDataSet
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.core.utils.DurationUtils
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.domain.usecase.CalculateFatigueUseCase
import com.example.logifitappp.domain.usecase.FetchActivitiesByShiftUseCase
import com.example.logifitappp.domain.usecase.FetchActivityAmountsByShiftUseCase
import com.example.logifitappp.domain.usecase.FetchNapAmountsByShiftUseCase
import com.example.logifitappp.domain.usecase.FindAppropriateSleepConditionUseCase
import com.example.logifitappp.viewmodel.states.SleepDetailState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = SleepDetailViewModel.SleepDetailViewModelFactory::class)
class SleepDetailViewModel @AssistedInject constructor(
    @Assisted private val mac: String,
    @Assisted private val user: UserModel?,
    private val calculateFatigueUseCase: CalculateFatigueUseCase,
    private val fetchActivitiesByShiftUseCase: FetchActivitiesByShiftUseCase,
    private val fetchActivityAmountsByShiftUseCase: FetchActivityAmountsByShiftUseCase,
    private val fetchNapAmountsByShiftUseCase: FetchNapAmountsByShiftUseCase,
    private val findAppropriateSleepConditionUseCase: FindAppropriateSleepConditionUseCase
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
                summary = App.context.getString(R.string.sleep_detail_summary, shift?.startTime ?: "N/A", shift?.endTime ?: "N/A"),
                tenant = App.database.tenantDao().find(user.tenantId)
            )
        }

        state = state.copy(wearable = App.wearableManager.getWearableByMac(mac))

        fetchInformation()
    }

    private fun checkIfCanGoToNext(calendar: Calendar): Boolean {
        return DateTimeUtils.format(calendar.time, "dd/MM/yyyy") != DateTimeUtils.format(Calendar.getInstance().time, "dd/MM/yyyy")
    }

    private fun fetchInformation() {
        if (state.shift == null || state.wearable == null) return

        val activities = fetchActivitiesByShiftUseCase(state.shift!!, state.wearable!!, state.date)
        val dataset = SleepDataSet(fetchActivityAmountsByShiftUseCase(state.shift!!, state.wearable!!, state.date))
        val sleepCondition = findAppropriateSleepConditionUseCase(dataset.amounts.totalSleepMinutes * 60, state.tenant!!)

        state = state.copy(
            fatigue = calculateFatigueUseCase(state.wearable!!, activities, dataset.amounts),
            nap = fetchNapAmountsByShiftUseCase(state.shift!!, state.wearable!!, state.date),
            sleepCondition = sleepCondition,
            sleepDataSet = dataset,
            subtitle = DurationUtils.format(dataset.amounts.totalSleepMinutes * 60)
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