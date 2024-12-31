package com.example.logifitappp.core.analyzers

import com.example.logifitappp.enums.Spo2ModeEnum
import java.util.Date
import kotlin.math.max
import kotlin.math.min

class Spo2Amount {
    lateinit var endDate: Date
    lateinit var startDate: Date

    val modes = mutableListOf<Spo2ModeEnum>()
    var minSpo2 = Int.MAX_VALUE
    var maxSpo2 = 0

    fun addMode(mode: Spo2ModeEnum) {
        println("mode: $mode")
        modes.add(mode)
    }

    fun setSpo2(spo2: Int) {
        minSpo2 = min(spo2, minSpo2)
        maxSpo2 = max(spo2, maxSpo2)
    }

    fun setEndDate(seconds: Long) {
        this.endDate = Date(seconds * 1000)
    }

    fun setStartDate(seconds: Long) {
        this.startDate = Date(seconds * 1000)
    }

    override fun toString() = "spo2 amount={start: $startDate, end: $endDate, min: $minSpo2, max: $maxSpo2}"
}