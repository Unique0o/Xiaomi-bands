package com.example.logifitappp.data.models

import java.time.LocalDateTime

data class DrowsinessModel(
    val id: Int,
    val createdAt: LocalDateTime,
    val sentAt: String? = null,
    val totalSleepSeconds: Int,
    val wearableInternalIdentifier: Int
)