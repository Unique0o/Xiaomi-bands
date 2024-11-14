package com.example.logifitappp.core.analyzers

import java.util.Date
import kotlin.math.max
import kotlin.math.min

class HeartRateAmount {
    lateinit var endDate: Date
    lateinit var startDate: Date

    var minHeartRate = Long.MAX_VALUE
    var maxHeartRate = 0L

    fun setHeartRate(heartRate: Int) {
        minHeartRate = min(heartRate.toLong(), minHeartRate)
        maxHeartRate = max(heartRate.toLong(), maxHeartRate)
    }

    fun setEndDate(seconds: Long) {
        this.endDate = Date(seconds * 1000)
    }

    fun setStartDate(seconds: Long) {
        this.startDate = Date(seconds * 1000)
    }

    override fun toString() = "hr amount={start: $startDate, end: $endDate, min: $minHeartRate, max: $maxHeartRate}"
}