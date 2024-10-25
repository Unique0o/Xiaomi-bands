package com.example.logifitappp.domain.usecase

import android.icu.util.GregorianCalendar
import com.example.logifitappp.core.analyzers.ActivityAnalyzer
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.SynchronizationProcessingException

class ProcessSynchronizedWearableDataUseCase {
    operator fun invoke(shift: ShiftModel?, wearable: Wearable) {
        if (shift == null) throw SynchronizationProcessingException(AppStatusCodeEnum.UNSELECTED_SHIFT)

        val now = GregorianCalendar.getInstance()
        val endTs = (now.timeInMillis / 1000).toInt()
        val startTs = endTs - 24 * 60 * 60 - 1

        val analyzer = ActivityAnalyzer()

        val activities = wearable
            .getWearableCoordinator()
            .getActivityProvider(wearable)
            .getRawActivitiesBetween(startTs, endTs)

        if (activities.isEmpty()) throw SynchronizationProcessingException(AppStatusCodeEnum.NO_SYNCHRONIZED_WEARABLE_DATA)

        val sleeps = analyzer.calculateSleepAmounts(activities)
    }
}