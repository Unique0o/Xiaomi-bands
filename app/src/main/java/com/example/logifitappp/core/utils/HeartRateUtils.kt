package com.example.logifitappp.core.utils

object HeartRateUtils {
    fun getMeasureLevelToLpm(lpm: Int) = when {
        lpm <= 100 -> "Reposo"
        lpm in 100 .. 114 -> "Muy suave"
        lpm in 114 .. 133 -> "Suave"
        lpm in 133 .. 152 -> "Moderado"
        lpm in 152 .. 172 -> "Intenso"
        lpm in 172 .. 255 -> "Máximo"
        else -> ""
    }
}