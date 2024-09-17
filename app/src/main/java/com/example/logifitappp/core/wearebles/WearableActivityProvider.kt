package com.example.logifitappp.core.wearebles

import com.example.logifitappp.data.dao.commons.WearableRawActivityDao
import com.example.logifitappp.data.models.commons.WearableRawActivityModel

abstract class WearableActivityProvider<T: WearableRawActivityModel>(wearable: Wearable): AbstractWearableProvider(wearable) {
    open fun getRawActivitiesBetween(from: Int, to: Int): List<T> {
        val activities = getWearableRawActivityDao()?.getRawActivitiesBetween(from, to, getStoredWearable()?.id ?: 0) ?: listOf()

        activities.forEach {
            it.provider = this
        }

        return activities
    }

    fun store(vararg samples: T) {
        val wearable = getStoredWearable()

        samples.forEach {
            it.wearableId = wearable?.id ?: 0
        }

        getWearableRawActivityDao()?.store(*samples)
    }

    abstract fun getWearableRawActivityDao(): WearableRawActivityDao<T>?
    abstract fun normalizeIntensity(intensity: Int): Float
    abstract fun normalizeType(type: Int): WearableActivityTypeEnum
}