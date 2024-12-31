package com.example.logifitappp.core.wearebles

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import com.example.logifitappp.data.dao.commons.WearableSpo2SampleDao
import com.example.logifitappp.data.models.commons.WearableSpo2SampleModel

abstract class WearableSpo2SampleProvider<T: WearableSpo2SampleModel>(wearable: Wearable): AbstractWearableProvider(wearable) {
    fun getSamplesBetween(from: Long, to: Long): List<T> {
        println("fetch spo2 samples between $from - $to")

        return getWearableSpo2SampleDao().getSamplesBetween(from, to, getStoredWearable()?.id ?: 0)
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

        getWearableSpo2SampleDao().store(*samples)
    }

    abstract fun getWearableSpo2SampleDao(): WearableSpo2SampleDao<T>
}