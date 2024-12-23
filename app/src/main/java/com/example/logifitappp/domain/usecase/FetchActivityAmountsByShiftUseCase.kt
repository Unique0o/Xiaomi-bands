package com.example.logifitappp.domain.usecase

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import com.example.logifitappp.core.analyzers.ActivityAmountList
import com.example.logifitappp.core.analyzers.ActivityAnalyzer
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.ShiftModel

class FetchActivityAmountsByShiftUseCase(private val fetchActivitiesByShiftUseCase: FetchActivitiesByShiftUseCase) {
    operator fun invoke(shift: ShiftModel, wearable: Wearable, baseCalendar: Calendar = GregorianCalendar.getInstance()): ActivityAmountList {
        val analyzer = ActivityAnalyzer()

        return analyzer.calculate(fetchActivitiesByShiftUseCase(shift, wearable, baseCalendar))
    }
}