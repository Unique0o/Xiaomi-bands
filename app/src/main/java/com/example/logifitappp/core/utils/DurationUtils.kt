package com.example.logifitappp.core.utils

import java.time.Duration

object DurationUtils {
    private const val HOURS_PER_DAY = 24
    private const val MINUTES_PER_HOUR = 60
    private const val SECONDS_PER_MINUTE = 60
    private const val SECONDS_PER_HOUR = MINUTES_PER_HOUR * SECONDS_PER_MINUTE
    private const val SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY

    fun format(seconds: Long): String {
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

        if (values.size < totalValues) values.add("${seconds % SECONDS_PER_MINUTE}s")

        return values.joinToString(separator = " ")
    }
}