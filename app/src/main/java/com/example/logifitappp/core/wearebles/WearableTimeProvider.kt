package com.example.logifitappp.core.wearebles

import com.example.logifitappp.data.dao.commons.WearableTimeDao
import com.example.logifitappp.data.models.commons.WearableTimeModel

abstract class WearableTimeProvider<T: WearableTimeModel>(wearable: Wearable) : AbstractWearableProvider(wearable) {
    fun getBetween(from: Long, to: Long): List<T> {
        return getWearableTimeDao().getBetween(from, to, getStoredWearable()?.id ?: 0)
    }

    fun getLastBeforeOf(timestamp: Long): T? {
        return getWearableTimeDao().getLastBeforeOf(timestamp, getStoredWearable()?.id ?: 0)
    }

    fun store(vararg samples: T) {
        val wearable = getStoredWearable()

        samples.forEach {
            it.wearableId = wearable?.id ?: 0
        }

        getWearableTimeDao().store(*samples)
    }

    abstract fun getWearableTimeDao(): WearableTimeDao<T>
}