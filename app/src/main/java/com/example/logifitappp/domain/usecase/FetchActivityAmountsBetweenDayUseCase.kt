package com.example.logifitappp.domain.usecase

import android.icu.util.Calendar
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.commons.WearableRawActivityModel

class FetchActivityAmountsBetweenDayUseCase {
    operator fun invoke(wearable: Wearable, calendar: Calendar): List<WearableRawActivityModel> {
        val coordinator = wearable.getWearableCoordinator()
        val provider = coordinator.getActivityProvider(wearable)

        return provider.getRawActivitiesBetweenDay(calendar)
    }
}