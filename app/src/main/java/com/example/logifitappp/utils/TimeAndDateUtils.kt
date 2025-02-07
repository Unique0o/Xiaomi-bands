package com.example.logifitappp.utils

object TimeAndDateUtils {
    fun formatSecondsToTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60

        return buildString {
            if (hours > 0) append("$hours hour${if (hours > 1) "s" else ""} ")
            if (minutes > 0) append("$minutes minute${if (minutes > 1) "s" else ""} ")
            if (remainingSeconds > 0 && hours == 0) append("$remainingSeconds second${if (remainingSeconds > 1) "s" else ""}")
        }.trim()
    }
}