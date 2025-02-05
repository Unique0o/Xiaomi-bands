package com.example.logifitappp.domain.usecase

import android.icu.util.Calendar
import com.example.logifitappp.core.analyzers.Spo2AmountList
import com.example.logifitappp.core.analyzers.Spo2Analyzer
import com.example.logifitappp.core.wearebles.Wearable

class FetchSpo2SampleAmountsBetweenDayUseCase {
    operator fun invoke(wearable: Wearable, calendar: Calendar): Spo2AmountList {
        val coordinator = wearable.getWearableCoordinator()

        if (!coordinator.supportsSpo2()) return Spo2AmountList()

        val provider = coordinator.getSpo2SampleProvider(wearable) ?: return Spo2AmountList()
        val spo2Analyzer = Spo2Analyzer()

        return spo2Analyzer.calculate(provider.getSamplesBetweenDay(calendar), calendar, 30)
    }
}