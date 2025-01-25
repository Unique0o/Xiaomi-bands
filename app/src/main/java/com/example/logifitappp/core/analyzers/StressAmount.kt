package com.example.logifitappp.core.analyzers

import java.util.Date
import kotlin.math.max
import kotlin.math.min

class StressAmount {
    lateinit var endDate: Date
    lateinit var startDate: Date

    var maxStress = 0
    var minStress = Int.MAX_VALUE

    fun setStress(stress: Int) {
        maxStress = max(maxStress, stress)
        minStress = min(minStress, stress)
    }

    fun setEndDate(seconds: Long) {
        this.endDate = Date(seconds * 1000)
    }

    fun setStartDate(seconds: Long) {
        this.startDate = Date(seconds * 1000)
    }

    override fun toString() = "stress amount={start: $startDate, end: $endDate, min: $minStress, max: $maxStress}"
}