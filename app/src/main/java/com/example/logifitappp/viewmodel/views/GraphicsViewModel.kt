package com.example.logifitappp.viewmodel.views

import android.icu.util.GregorianCalendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.logifitappp.core.App
import com.example.logifitappp.core.analyzers.HeartRateAnalyzer
import com.example.logifitappp.core.analyzers.StepsAnalyzer
import com.example.logifitappp.core.graphics.HeartRateDataSet
import com.example.logifitappp.core.graphics.SleepBarDataSet
import com.example.logifitappp.core.graphics.StepsBarDataSet
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.domain.usecase.FetchActivityAmountsBetweenDayUseCase
import com.example.logifitappp.domain.usecase.FetchActivityAmountsByShiftUseCase
import com.example.logifitappp.viewmodel.states.GraphicsState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = GraphicsViewModel.GraphicsViewModelFactory::class)
class GraphicsViewModel  @AssistedInject constructor(
    @Assisted private val user: UserModel,
    private val fetchActivityAmountsBetweenDayUseCase: FetchActivityAmountsBetweenDayUseCase,
    private val fetchActivityAmountsByShiftUseCase: FetchActivityAmountsByShiftUseCase
): ViewModel() {
    @AssistedFactory
    interface GraphicsViewModelFactory {
        fun create(user: UserModel): GraphicsViewModel
    }

    var state by mutableStateOf(GraphicsState())
        private set

    init {
        user.shiftId?.let {
            val wearable = App.wearableManager.getWearables().first()
            val shift = App.database.shiftDao().find(it)!!

            calculate(shift, wearable)
        }
    }

    private fun calculate(shift: ShiftModel, wearable: Wearable) {
        val calendar = GregorianCalendar.getInstance()
        val activitiesFromToday = fetchActivityAmountsBetweenDayUseCase(wearable, calendar)

        val heartRateAnalyzer = HeartRateAnalyzer()
        val stepsAnalyzer = StepsAnalyzer()

        state = state.copy(
            heartRateDataSet = HeartRateDataSet(heartRateAnalyzer.calculate(activitiesFromToday, calendar, 30)),
            shift = shift,
            sleepDataSet = SleepBarDataSet(fetchActivityAmountsByShiftUseCase(shift, wearable)),
            stepsDataset = StepsBarDataSet(stepsAnalyzer.calculate(activitiesFromToday, calendar, 30)),
        )
    }
}