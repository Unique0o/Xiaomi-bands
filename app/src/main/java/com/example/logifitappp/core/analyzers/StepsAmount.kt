package com.example.logifitappp.core.analyzers

import java.util.Date
import kotlin.math.max

class StepsAmount {
    lateinit var endDate: Date
    lateinit var startDate: Date

    var totalSteps = 0L

    fun addSteps(steps: Int) {
        totalSteps += max(steps, 0)
    }

    fun setEndDate(seconds: Long) {
        this.endDate = Date(seconds * 1000)
    }

    fun setStartDate(seconds: Long) {
        this.startDate = Date(seconds * 1000)
    }
}