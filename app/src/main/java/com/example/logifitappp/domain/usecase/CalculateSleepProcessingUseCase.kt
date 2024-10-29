package com.example.logifitappp.domain.usecase

import com.example.logifitappp.core.App
import com.example.logifitappp.core.analyzers.ActivityAnalyzer
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.DrowsinessModel
import com.example.logifitappp.data.models.FatigueModel
import com.example.logifitappp.data.models.ShiftModel

class CalculateSleepProcessingUseCase {
    operator fun invoke(shift: ShiftModel, wearable: Wearable) {
        val user = App.database.userDao().getLoggedIn()!!
        val wearableModel = App.database.wearableDao().find(wearable.getAddress()!!, user.id)!!

        val startTs = shift.getStartDateTimestamp().timeInMillis / 1000
        val endTs = shift.getEndDateTimestamp().timeInMillis / 1000

        val analyzer = ActivityAnalyzer()
        val coordinator = wearable.getWearableCoordinator()
        val provider = coordinator.getActivityProvider(wearable)

        val activities = provider.getRawActivitiesBetween(startTs, endTs)
        val amounts = analyzer.calculateActivityAmounts(activities)

        println("CalculateSleepProcessingUseCase: $amounts in $startTs to $endTs")

        App.database
            .drowsinessDao()
            .findFromToday(wearableModel.id)
            ?.let {
                it.totalSleepSeconds = amounts.totalSleepMinutes * 60
                App.database.drowsinessDao().store(it)
            }
            ?: App.database.drowsinessDao().store(DrowsinessModel(
                totalSleepSeconds = amounts.totalSleepMinutes * 60,
                wearableId = wearableModel.id
            ))

        if (amounts.totalSleepMinutes <= 0) return

        val withHypertension = activities.any {
            it.isHeartRateValid() && it.isSleep() && (it.heartRate < 40 || it.heartRate > 100)
        }

        App.database
            .fatigueDao()
            .findFromToday(wearableModel.id)
            ?.let {
                if (coordinator.supportsRemSleep()) {
                    it.reemCycles = amounts.remCycles
                    it.totalReemSeconds = amounts.totalRemSleepMinutes * 60
                    it.withLittleReemSleep = (amounts.totalRemSleepMinutes * 100 / amounts.totalSleepMinutes) < 15
                }

                if (coordinator.supportsHeartRateMeasurement()) {
                    it.withHypertension = withHypertension
                }

                it.totalAwakeSeconds = amounts.totalAwakeningMinutes * 60
                it.totalSleepSeconds = amounts.totalSleepMinutes * 60
                it.withAwakeningOvercome = amounts.maxAwakeningMinutes > 20
                it.withLittleSleep =  amounts.totalSleepMinutes < 60
                it.withLongAwake = amounts.totalAwakeningMinutes > 60

                App.database.fatigueDao().store(it)
            }
            ?: App.database.fatigueDao().store(FatigueModel(
                reemCycles = if (coordinator.supportsRemSleep()) amounts.remCycles else null,
                totalReemSeconds = if (coordinator.supportsRemSleep()) amounts.totalRemSleepMinutes * 60 else null,
                totalAwakeSeconds = amounts.totalAwakeningMinutes * 60,
                totalSleepSeconds = amounts.totalSleepMinutes * 60,
                wearableId = wearableModel.id,
                withAwakeningOvercome = amounts.maxAwakeningMinutes > 20,
                withHypertension = if (coordinator.supportsHeartRateMeasurement()) withHypertension else false,
                withLittleReemSleep = if (coordinator.supportsRemSleep()) (amounts.totalRemSleepMinutes * 100 / amounts.totalSleepMinutes) < 15 else null,
                withLittleSleep =  amounts.totalSleepMinutes < 60,
                withLongAwake = amounts.totalAwakeningMinutes > 60
            ))
    }
}