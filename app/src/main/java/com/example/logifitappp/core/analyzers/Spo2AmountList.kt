package com.example.logifitappp.core.analyzers

import kotlin.math.max
import kotlin.math.min

class Spo2AmountList {
    private val amounts = mutableListOf<Spo2Amount>()

    var averageSpo2 = 0f
    var latestMeasuredSpo2 = 0
    var maxMeasuredSpo2 = 0
    var minMeasuredSpo2 = Int.MAX_VALUE

    fun add(amount: Spo2Amount) {
        amounts.add(amount)
        maxMeasuredSpo2 = max(amount.maxSpo2, maxMeasuredSpo2)

        if (amount.minSpo2 > 0) minMeasuredSpo2 = min(amount.minSpo2, minMeasuredSpo2)
    }

    fun getList() = amounts
}