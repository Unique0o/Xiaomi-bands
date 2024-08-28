package com.example.logifitappp.data.models

import java.time.LocalDateTime

data class SleepModel(
    val id: Int,
    val createdAt: LocalDateTime,
    val deepSleepSeconds: Int,
    val endAt: String,
    val interruptions: Int,
    val lightSleepSeconds: Int,
    val reemSleepSeconds: Int,
    val startAt: String,
    val totalSleepSeconds: Int,
    val wearableInternalIdentifier: Int
)