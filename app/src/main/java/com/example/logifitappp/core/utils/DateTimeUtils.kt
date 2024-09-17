package com.example.logifitappp.core.utils

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

    fun formatIso8601(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US).format(date)
    }
}