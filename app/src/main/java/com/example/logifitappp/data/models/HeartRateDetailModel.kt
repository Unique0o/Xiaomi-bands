package com.example.logifitappp.data.models

import java.time.LocalDate

data class HeartRateDetailModel(
    val averageHeartRate: Double,
    val date: LocalDate,
    val latestMeasuredHeartRate: Int,
    val maxMeasuredHeartRate: Int,
    val minMeasuredHeartRate: Int,
    val rangeTimeInformation: String
)