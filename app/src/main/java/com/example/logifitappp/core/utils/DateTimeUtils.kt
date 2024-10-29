package com.example.logifitappp.core.utils

import android.icu.util.GregorianCalendar
import android.text.format.DateUtils
import com.example.logifitappp.core.App
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtils {
    fun formatDateTime(date: Date): String {
        return DateUtils.formatDateTime(
            App.context,
            date.time,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_NO_YEAR
        )
    }

    fun formatExtendedIso8601(date: Date): String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(date)

    fun formatIso8601(date: Date): String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US).format(date)

    fun formatReducedIso8601(date: Date): String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date)

    fun parse(date: String, format: String): Date? = SimpleDateFormat(format, Locale.US).parse(date)

    fun parse(date: String, from: String, to: String): String {
        val inputFormat = SimpleDateFormat(from, Locale.US)
        val outFormat = SimpleDateFormat(to, Locale.US)

        val possibleDate = inputFormat.parse(date)

        return possibleDate?.let { outFormat.format(it) } ?: ""
    }

    fun setTime(calendar: GregorianCalendar, date: String): GregorianCalendar {
        val outdatedCalendar = GregorianCalendar().apply {
            time = parse(date, "HH:mm:ss")
        }

        calendar.set(GregorianCalendar.HOUR_OF_DAY, outdatedCalendar.get(GregorianCalendar.HOUR_OF_DAY))
        calendar.set(GregorianCalendar.MINUTE, outdatedCalendar.get(GregorianCalendar.MINUTE))
        calendar.set(GregorianCalendar.SECOND, outdatedCalendar.get(GregorianCalendar.SECOND))

        return calendar
    }
}