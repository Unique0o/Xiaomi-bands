package com.example.logifitappp.data.models

import java.time.LocalDateTime

data class SleepEntry(
    val id: Int,
    val fellAsleepTime: LocalDateTime,
    val wokeUpTime: LocalDateTime
)