package com.example.logifitappp.core.analyzers

import kotlin.math.max
import kotlin.math.min

class StressAmountList {
    private val amounts = mutableListOf<StressAmount>()

    var averageStress = 0f
    var latestMeasuredStress = 0
    var maxMeasuredStress = 0
    var minMeasuredStress = Int.MAX_VALUE

    fun add(amount: StressAmount) {
        amounts.add(amount)
        maxMeasuredStress = max(amount.maxStress, maxMeasuredStress)

        if (amount.minStress > 0) minMeasuredStress = min(amount.minStress, minMeasuredStress)
    }

    fun getList() = amounts
}