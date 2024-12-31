package com.example.logifitappp.core.wearebles

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import com.example.logifitappp.data.dao.commons.WearableRawActivityDao
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.data.models.commons.WearableRawActivityModel
import com.example.logifitappp.enums.WearableActivityTypeEnum

abstract class WearableActivityProvider<T: WearableRawActivityModel>(wearable: Wearable): AbstractWearableProvider(wearable) {
    fun findLastRawActivity(): T? {
        return getWearableRawActivityDao()?.findLastActivity(getStoredWearable()?.id ?: 0)
    }

    fun getRawActivities(shift: ShiftModel, baseCalendar: Calendar = GregorianCalendar.getInstance()): List<T> {
        val startTs = shift.getStartDateTimestamp(baseCalendar).timeInMillis / 1000
        val endTs = shift.getEndDateTimestamp(baseCalendar).timeInMillis / 1000

        return getRawActivitiesBetween(startTs, endTs)
    }

    open fun getRawActivitiesBetween(from: Long, to: Long): List<T> {
        println("fetch activities between $from - $to")

        val activities = getWearableRawActivityDao()?.getRawActivitiesBetween(from, to, getStoredWearable()?.id ?: 0) ?: listOf()

        activities.forEach {
            it.provider = this
        }

        return activities
    }

    fun getRawActivitiesBetweenDay(calendar: Calendar): List<T> {
        calendar.apply {
            set(GregorianCalendar.HOUR_OF_DAY, 0)
            set(GregorianCalendar.MINUTE, 0)
            set(GregorianCalendar.SECOND, 0)
            set(GregorianCalendar.MILLISECOND, 0)
        }

        val startTs = calendar.timeInMillis / 1000
        val endTs = startTs + 24 * 60 * 60

        return getRawActivitiesBetween(startTs, endTs)
    }

    fun getRawActivitiesFromLast24h(): List<T> {
        val now = GregorianCalendar.getInstance()
        val endTs = now.timeInMillis / 1000
        val startTs = endTs - 24 * 60 * 60 - 1

        return getRawActivitiesBetween(startTs, endTs)
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