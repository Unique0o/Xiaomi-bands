package com.example.logifitappp.data.models

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
    fun getEndDateTimestamp(): GregorianCalendar {
        return DateTimeUtils.setTime(GregorianCalendar(), endTime)
    }

    fun getStartDateTimestamp(baseCalendar: GregorianCalendar = GregorianCalendar()): GregorianCalendar {
        val startTimestamp = DateTimeUtils.setTime(GregorianCalendar(), startTime)
        val endTimestamp = getEndDateTimestamp()

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
}