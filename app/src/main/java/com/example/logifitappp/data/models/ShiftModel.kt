package com.example.logifitappp.data.models

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.logifitappp.core.utils.DateTimeUtils

@Entity(tableName = "shifts")
data class ShiftModel(
    @ColumnInfo(name = "days_to_apply_sleep_time_extension") val daysToApplySleepTimeExtension: String? = null,
    @ColumnInfo(name = "end_time") val endTime: String,
    @PrimaryKey val id: Int,
    val name: String,
    @ColumnInfo(name = "sleep_time_extension_hours") val sleepTimeExtensionHours: Int? = null,
    @ColumnInfo(name = "start_time") val startTime: String,
    @ColumnInfo(name = "tenant_id") val tenantId: Int
) {
    fun getEndDateTimestamp(baseCalendar: Calendar = GregorianCalendar.getInstance()): Calendar {
        return DateTimeUtils.setTime(baseCalendar, endTime)
    }

    fun getNapRangePair(baseCalendar: Calendar = GregorianCalendar.getInstance()): Pair<Long, Long> {
        val startAt = getStartDateTimestamp(baseCalendar)
        val endAt = getEndDateTimestamp(baseCalendar)

        val end = endAt.clone() as Calendar
        val start = (endAt.clone() as Calendar).apply {
            add(Calendar.DAY_OF_MONTH, -1)
        }

        if (DateTimeUtils.getSecondsOfDay(startAt) >= DateTimeUtils.getSecondsOfDay(endAt)) {
            end.apply {
                set(Calendar.DAY_OF_MONTH, startAt.get(Calendar.DAY_OF_MONTH))
                set(Calendar.MONTH, startAt.get(Calendar.MONTH))
                set(Calendar.YEAR, startAt.get(Calendar.YEAR))
            }
        } else {
            end.apply {
                set(Calendar.DAY_OF_MONTH, start.get(Calendar.DAY_OF_MONTH))
                set(Calendar.MONTH, start.get(Calendar.MONTH))
                set(Calendar.YEAR, start.get(Calendar.YEAR))
            }
        }

        return Pair(end.timeInMillis / 1000, startAt.timeInMillis / 1000)
    }

    fun getStartDateTimestamp(baseCalendar: Calendar = GregorianCalendar.getInstance()): Calendar {
        val startTimestamp = DateTimeUtils.setTime(baseCalendar, startTime)
        val endTimestamp = getEndDateTimestamp(baseCalendar)

        if (daysToApplySleepTimeExtension != null) {
            val dayOfWeek = baseCalendar.get(GregorianCalendar.DAY_OF_WEEK)
            val currentDayOfWeek = if (dayOfWeek == 1) 7 else dayOfWeek - 1
            val mDaysToApply24Hours = daysToApplySleepTimeExtension.split(",")

            if (mDaysToApply24Hours.contains(currentDayOfWeek.toString())) {
                endTimestamp.add(GregorianCalendar.HOUR_OF_DAY, -(sleepTimeExtensionHours ?: 24))

                return endTimestamp
            }
        }

        if (!startTimestamp.before(endTimestamp)) startTimestamp.add(GregorianCalendar.DAY_OF_MONTH, -1)

        return startTimestamp
    }

    override fun toString() = name
}