package com.example.logifitappp.core.utils

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.text.format.DateUtils
import com.example.logifitappp.core.App
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateTimeUtils {
    fun formatDateTime(date: Date): String {
        return DateUtils.formatDateTime(
            App.context,
            date.time,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_NO_YEAR
        )
    }

    fun format(date: Date, format: String, timeZone: TimeZone): String = SimpleDateFormat(format, Locale.US).apply {
        this.timeZone = timeZone
    }.format(date)

    fun format(date: Date, format: String): String = SimpleDateFormat(format, Locale.US).format(date)

    fun formatExtendedIso8601(date: Date): String = format(date, "yyyy-MM-dd HH:mm:ss")

    fun formatIso8601(date: Date): String = format(date, "yyyy-MM-dd'T'HH:mm:ssXXX")

    fun formatReducedIso8601(date: Date): String = format(date, "yyyy-MM-dd")

    fun parse(date: String, format: String): Date? = SimpleDateFormat(format, Locale.US).parse(date)

    fun parse(time: Long, format: String, timeZone: TimeZone): String {
        val date = GregorianCalendar.getInstance().apply {
            timeInMillis = time
        }

        return format(date.time, format, timeZone)
    }

    fun parse(date: String, from: String, to: String): String {
        val inputFormat = SimpleDateFormat(from, Locale.US)
        val outFormat = SimpleDateFormat(to, Locale.US)

        val possibleDate = inputFormat.parse(date)

        return possibleDate?.let { outFormat.format(it) } ?: ""
    }

    fun setTime(calendar: Calendar, date: String): Calendar {
        val clonedCalendar =  GregorianCalendar.getInstance().apply {
            timeInMillis = calendar.timeInMillis
        }

        val outdatedCalendar = GregorianCalendar.getInstance().apply {
            time = parse(date, "HH:mm:ss")
        }

        clonedCalendar.set(GregorianCalendar.HOUR_OF_DAY, outdatedCalendar.get(GregorianCalendar.HOUR_OF_DAY))
        clonedCalendar.set(GregorianCalendar.MINUTE, outdatedCalendar.get(GregorianCalendar.MINUTE))
        clonedCalendar.set(GregorianCalendar.SECOND, outdatedCalendar.get(GregorianCalendar.SECOND))

        return clonedCalendar
    }
}