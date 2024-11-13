package com.example.logifitappp.core.analyzers

import kotlin.math.max
import kotlin.math.min

class HeartRateAmountList {
    private val amounts = mutableListOf<HeartRateAmount>()

    var averageHeartRate = 0f
    var latestMeasuredHeartRate = 0L
    var maxMeasuredHeartRate = 0L
    var minMeasuredHeartRate = Long.MAX_VALUE

    fun add(amount: HeartRateAmount) {
        amounts.add(amount)

        maxMeasuredHeartRate = max(amount.maxHeartRate, maxMeasuredHeartRate)
        minMeasuredHeartRate = min(amount.minHeartRate, minMeasuredHeartRate)
    }

    fun getList() = amounts
}