package com.example.logifitappp.domain.usecase

import com.example.logifitappp.core.App
import com.example.logifitappp.core.analyzers.ActivityAnalyzer
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.DrowsinessModel
import com.example.logifitappp.data.models.FatigueModel
import com.example.logifitappp.data.models.ShiftModel

class CalculateSleepProcessingUseCase {
    operator fun invoke(shift: ShiftModel, wearable: Wearable) {
        val analyzer = ActivityAnalyzer()
        val coordinator = wearable.getWearableCoordinator()
        val provider = coordinator.getActivityProvider(wearable)
        val wearableModel = provider.getStoredWearable()!!

        val activities = provider.getRawActivities(shift)
        val amounts = analyzer.calculate(activities)

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

        val withHypertension = activities.any {
            it.isHeartRateValid() && it.isSleep() && (it.heartRate < 40 || it.heartRate > 100)
        }

        App.database
            .fatigueDao()
            .findFromToday(wearableModel.id)
            ?.let {
                if (coordinator.supportsRemSleep()) {
                    it.remCycles = amounts.remCycles
                    it.totalRemSeconds = amounts.totalRemSleepMinutes * 60
                    it.withLittleReemSleep = amounts.remSleepPercentage < 15
                }

                if (coordinator.supportsHeartRateMeasurement()) {
                    it.withHypertension = withHypertension
                }

                it.totalAwakeSeconds = amounts.totalAwakeningMinutes * 60
                it.totalSleepSeconds = amounts.totalSleepMinutes * 60
                it.withAwakeningOvercome = amounts.maxAwakeningMinutes > 20
                it.withLittleSleep =  amounts.totalSleepMinutes < 60 * 6
                it.withLongAwake = amounts.totalAwakeningMinutes > 60

                App.database.fatigueDao().store(it)
            }
            ?: App.database.fatigueDao().store(FatigueModel(
                remCycles = if (coordinator.supportsRemSleep()) amounts.remCycles else null,
                totalRemSeconds = if (coordinator.supportsRemSleep()) amounts.totalRemSleepMinutes * 60 else null,
                totalAwakeSeconds = amounts.totalAwakeningMinutes * 60,
                totalSleepSeconds = amounts.totalSleepMinutes * 60,
                wearableId = wearableModel.id,
                withAwakeningOvercome = amounts.maxAwakeningMinutes > 20,
                withHypertension = if (coordinator.supportsHeartRateMeasurement()) withHypertension else false,
                withLittleReemSleep = if (coordinator.supportsRemSleep()) amounts.remSleepPercentage < 15 else null,
                withLittleSleep =  amounts.totalSleepMinutes < 60 * 6,
                withLongAwake = amounts.totalAwakeningMinutes > 60
            ))
    }
}