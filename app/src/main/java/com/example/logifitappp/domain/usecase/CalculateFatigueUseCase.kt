package com.example.logifitappp.domain.usecase

import com.example.logifitappp.core.analyzers.ActivityAmountList
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.FatigueModel
import com.example.logifitappp.data.models.commons.WearableRawActivityModel

class CalculateFatigueUseCase {
    operator fun invoke(wearable: Wearable, activities: List<WearableRawActivityModel>, amounts: ActivityAmountList): FatigueModel {
        val coordinator = wearable.getWearableCoordinator()
        val provider = coordinator.getActivityProvider(wearable)
        val wearableModel = provider.getStoredWearable()!!

        val withHypertension = activities.any {
            it.isHeartRateValid() && it.isSleep() && (it.heartRate < 40 || it.heartRate > 100)
        }

        return FatigueModel(
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
        )
    }
}