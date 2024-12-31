package com.example.logifitappp.domain.usecase

import android.icu.util.Calendar
import com.example.logifitappp.core.analyzers.Spo2AmountList
import com.example.logifitappp.core.analyzers.Spo2Analyzer
import com.example.logifitappp.core.wearebles.Wearable

class FetchSpo2SampleAmountsBetweenDayUseCase {
    operator fun invoke(wearable: Wearable, calendar: Calendar): Spo2AmountList {
        val coordinator = wearable.getWearableCoordinator()
        val provider = coordinator.getSpo2SampleProvider(wearable)
        val spo2Analyzer = Spo2Analyzer()

        return spo2Analyzer.calculate(provider?.getSamplesBetweenDay(calendar) ?: listOf(), calendar, 30)
    }
}