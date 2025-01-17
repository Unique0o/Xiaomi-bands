package com.example.logifitappp.core.wearebles

import com.example.logifitappp.data.models.commons.WearableRawActivityModel
import com.example.logifitappp.data.models.commons.WearableSampleModel

abstract class AbstractActivityToSampleProvider<T: WearableSampleModel, S: WearableRawActivityModel>(wearable: Wearable): AbstractWearableSampleProvider<T>(wearable) {
    override fun getSamplesBetween(from: Long, to: Long): List<T> {
        val activityProvider = wearable.getWearableCoordinator().getActivityProvider(wearable)
        val activities = activityProvider.getRawActivitiesBetween(from / 1000, to / 1000)

        val samples = mutableListOf<T>()

        for (activity in activities) convertActivity(activity as S)?.let { samples.add(it) }

        return samples
    }

    override fun getWearableSampleDao() = null

    abstract fun convertActivity(activity: S): T?
}