package com.example.logifitappp.domain.usecase

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.data.models.commons.WearableRawActivityModel

class FetchActivitiesByShiftUseCase {
    operator fun invoke(shift: ShiftModel, wearable: Wearable, baseCalendar: Calendar = GregorianCalendar.getInstance()): List<WearableRawActivityModel> {
        val coordinator = wearable.getWearableCoordinator()
        val provider = coordinator.getActivityProvider(wearable)

        return provider.getRawActivities(shift, baseCalendar)
    }
}