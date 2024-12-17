package com.example.logifitappp.core.utils

object MathUtils {
    fun percentage(value: Long, total: Long): Float {
        if (total == 0L) return 0f

        return (value / total.toFloat()) * 100
    }
}