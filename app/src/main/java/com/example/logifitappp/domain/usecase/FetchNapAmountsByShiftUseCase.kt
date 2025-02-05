package com.example.logifitappp.domain.usecase

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import com.example.logifitappp.core.analyzers.ActivityAmountList
import com.example.logifitappp.core.analyzers.ActivityAnalyzer
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.ShiftModel

class FetchNapAmountsByShiftUseCase {
    operator fun invoke(shift: ShiftModel, wearable: Wearable, baseCalendar: Calendar = GregorianCalendar.getInstance()): ActivityAmountList {
        val analyzer = ActivityAnalyzer()
        val timestamps = shift.getNapRangePair(baseCalendar)

        val coordinator = wearable.getWearableCoordinator()
        val provider = coordinator.getActivityProvider(wearable)
        val activities = provider.getRawActivitiesBetween(timestamps.first, timestamps.second)

        return analyzer.calculate(activities)
    }
}