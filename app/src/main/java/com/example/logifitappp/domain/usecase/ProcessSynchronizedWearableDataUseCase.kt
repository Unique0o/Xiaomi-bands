package com.example.logifitappp.domain.usecase

import android.icu.util.GregorianCalendar
import com.example.logifitappp.core.App
import com.example.logifitappp.core.analyzers.ActivityAnalyzer
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.SynchronizationProcessingException
import javax.inject.Inject

class ProcessSynchronizedWearableDataUseCase @Inject constructor(
    private val calculateSleepProcessingUseCase: CalculateSleepProcessingUseCase
) {
    operator fun invoke(shift: ShiftModel?, wearable: Wearable) {
        if (shift == null) throw SynchronizationProcessingException(AppStatusCodeEnum.UNSELECTED_SHIFT)

        val provider = wearable
            .getWearableCoordinator()
            .getActivityProvider(wearable)

        val activities = provider.getRawActivitiesFromLast24h()

        val analyzer = ActivityAnalyzer()

        if (activities.isEmpty()) throw SynchronizationProcessingException(AppStatusCodeEnum.NO_SYNCHRONIZED_WEARABLE_DATA)

        val wearableModel = provider.getStoredWearable()!!

        calculateSleepProcessingUseCase(shift, wearable)

        App.database.sleepDao().apply {
            deleteFromDate(wearableModel.id, DateTimeUtils.formatReducedIso8601(GregorianCalendar.getInstance().time))
            store(*analyzer.calculate(activities).getSleeps(wearableModel.id).toTypedArray())
        }
    }
}