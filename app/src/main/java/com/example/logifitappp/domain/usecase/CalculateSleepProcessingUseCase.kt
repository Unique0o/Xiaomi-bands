package com.example.logifitappp.domain.usecase

import com.example.logifitappp.core.App
import com.example.logifitappp.core.analyzers.ActivityAnalyzer
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.DrowsinessModel
import com.example.logifitappp.data.models.ShiftModel

class CalculateSleepProcessingUseCase(private val calculateFatigueUseCase: CalculateFatigueUseCase) {
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

        val calculatedFatigue = calculateFatigueUseCase(wearable, activities, amounts)
        val storedFatigue = App.database.fatigueDao().findFromToday(wearableModel.id)

        if (storedFatigue != null) calculatedFatigue.id = storedFatigue.id

        App.database.fatigueDao().store(calculatedFatigue)
    }
}