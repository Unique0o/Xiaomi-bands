package com.example.logifitappp.core.utils

import android.content.Context
import com.example.logifitappp.R
import java.time.Duration
import java.util.Locale

object DurationUtils {
    private const val HOURS_PER_DAY = 24
    private const val MINUTES_PER_HOUR = 60
    private const val SECONDS_PER_MINUTE = 60
    private const val SECONDS_PER_HOUR = MINUTES_PER_HOUR * SECONDS_PER_MINUTE
    private const val SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY

    fun format(seconds: Long, requireSeconds: Boolean = false): String {
        val duration = Duration.ofSeconds(seconds)

        val values = mutableListOf<String>()
        val totalValues = 2

        (seconds / SECONDS_PER_DAY).let {
            if (it > 0) values.add("${it}d")
        }

        (duration.toHours() % HOURS_PER_DAY).let {
            if (it > 0) values.add("${it}h")
        }

        if (values.size < totalValues) values.add("${duration.toMinutes() % MINUTES_PER_HOUR}min")

        if (values.size < totalValues || requireSeconds) values.add("${seconds % SECONDS_PER_MINUTE}s")

        return values.joinToString(separator = " ")
    }

    fun formatExtended(context: Context, seconds: Long): String {
        val duration = Duration.ofSeconds(seconds)

        val values = mutableListOf<String>()
        val totalValues = 2

        (seconds / SECONDS_PER_DAY).let {
            if (it > 0) values.add("$it ${context.resources.getQuantityString(R.plurals.day_label, it.toInt())}")
        }

        (duration.toHours() % HOURS_PER_DAY).let {
            if (it > 0) values.add("$it ${context.resources.getQuantityString(R.plurals.hour_label, it.toInt())}")
        }

        if (values.size < totalValues) {
            (duration.toMinutes() % MINUTES_PER_HOUR).let {
                values.add("$it ${context.resources.getQuantityString(R.plurals.minute_label, it.toInt())}")
            }
        }

        return values.joinToString(separator = " ${context.getString(R.string.and)} ")
    }

    fun formatTime(seconds: Long): String {
        val duration = Duration.ofSeconds(seconds)

        val values = mutableListOf<String>()
        val totalValues = 2

        (seconds / SECONDS_PER_DAY).let {
            if (it > 0) values.add(String.format(Locale.US, "%02d", it))
        }

        (duration.toHours() % HOURS_PER_DAY).let {
            if (it > 0) values.add(String.format(Locale.US, "%02d", it))
        }

        if (values.size < totalValues) values.add(String.format(Locale.US, "%02d", duration.toMinutes() % MINUTES_PER_HOUR))

        if (values.size < totalValues) values.add(String.format(Locale.US, "%02d", seconds % SECONDS_PER_MINUTE))

        return values.joinToString(separator = ":")
    }
}