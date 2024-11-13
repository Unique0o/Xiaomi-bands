package com.example.logifitappp.core.analyzers

import kotlin.math.max

class StepsAmountList {
    private val amounts = mutableListOf<StepsAmount>()

    var maxStepsAmount = 0L
    var totalSteps = 0L

    fun add(amount: StepsAmount) {
        amounts.add(amount)
        
        totalSteps += max(amount.totalSteps, 0)
        maxStepsAmount = max(maxStepsAmount, amount.totalSteps)
    }

    fun getList() = amounts
}