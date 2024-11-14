package com.example.logifitappp.core.utils

import java.text.NumberFormat
import java.util.Locale

object StepsUtils {
    fun calculateKcal(steps: Long) = 0.02047592695 * steps

    fun calculateDistance(steps: Long) = steps * 175 * 0.43 * 0.01

    fun formatDistance(distance: Double): String {
        val formatInstance = NumberFormat.getNumberInstance(Locale.US)

        return if (distance < 1000) "${formatInstance.format(distance)}m"
        else "${formatInstance.format(distance / 1000)}km"
    }
}