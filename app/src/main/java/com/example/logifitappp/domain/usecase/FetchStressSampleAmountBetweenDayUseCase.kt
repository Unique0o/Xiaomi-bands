package com.example.logifitappp.domain.usecase

import android.icu.util.Calendar
import com.example.logifitappp.core.analyzers.StressAmountList
import com.example.logifitappp.core.analyzers.StressAnalyzer
import com.example.logifitappp.core.wearebles.Wearable

class FetchStressSampleAmountBetweenDayUseCase {
    operator fun invoke(wearable: Wearable, calendar: Calendar): StressAmountList {
        val coordinator = wearable.getWearableCoordinator()

        if (!coordinator.supportsSpo2()) return StressAmountList()

        val provider = coordinator.getStressSampleProvider(wearable) ?: return StressAmountList()
        val stressAnalyzer = StressAnalyzer()

        return stressAnalyzer.calculate(provider.getSamplesBetweenDay(calendar), calendar, 30)
    }
}