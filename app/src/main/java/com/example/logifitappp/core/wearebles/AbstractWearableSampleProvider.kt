package com.example.logifitappp.core.wearebles

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import com.example.logifitappp.data.dao.commons.WearableSampleDao
import com.example.logifitappp.data.models.commons.WearableSampleModel

abstract class AbstractWearableSampleProvider<T: WearableSampleModel>(wearable: Wearable): AbstractWearableProvider(wearable) {
    fun delete() {
        getWearableSampleDao()?.delete(getStoredWearable()?.id ?: 0)
    }

    open fun getSamplesBetween(from: Long, to: Long): List<T> {
        println("fetch spo2 samples between $from - $to")

        return getWearableSampleDao()?.getSamplesBetween(from, to, getStoredWearable()?.id ?: 0) ?: listOf()
    }

    fun getSamplesBetweenDay(calendar: Calendar): List<T> {
        calendar.apply {
            set(GregorianCalendar.HOUR_OF_DAY, 0)
            set(GregorianCalendar.MINUTE, 0)
            set(GregorianCalendar.SECOND, 0)
            set(GregorianCalendar.MILLISECOND, 0)
        }

        val startTs = calendar.timeInMillis
        val endTs = startTs + 24 * 60 * 60 * 1000

        return getSamplesBetween(startTs, endTs)
    }

    fun store(vararg samples: T) {
        val wearable = getStoredWearable()

        samples.forEach {
            it.wearableId = wearable?.id ?: 0
        }

        getWearableSampleDao()?.store(*samples)
    }

    abstract fun getWearableSampleDao(): WearableSampleDao<T>?
}