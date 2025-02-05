package com.example.logifitappp.data.models

import java.time.LocalDateTime

data class SleepEntry(
    val fellAsleepTime: LocalDateTime = LocalDateTime.now(),
    val wokeUpTime: LocalDateTime = LocalDateTime.now(),
    val duration: String? = null
)