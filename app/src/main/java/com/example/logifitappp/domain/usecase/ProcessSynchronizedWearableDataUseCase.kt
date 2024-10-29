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

        val now = GregorianCalendar.getInstance()
        val endTs = now.timeInMillis / 1000
        val startTs = endTs - 24 * 60 * 60 - 1

        val activities = wearable
            .getWearableCoordinator()
            .getActivityProvider(wearable)
            .getRawActivitiesBetween(startTs, endTs)

        val analyzer = ActivityAnalyzer()

        if (activities.isEmpty()) throw SynchronizationProcessingException(AppStatusCodeEnum.NO_SYNCHRONIZED_WEARABLE_DATA)

        val user = App.database.userDao().getLoggedIn()!!
        val wearableModel = App.database.wearableDao().find(wearable.getAddress()!!, user.id)!!

        calculateSleepProcessingUseCase(shift, wearable)

        App.database.sleepDao().apply {
            deleteFromDate(wearableModel.id, DateTimeUtils.formatReducedIso8601(now.time))
            store(*analyzer.calculateActivityAmounts(activities).getSleeps(wearableModel.id).toTypedArray())
        }
    }
}