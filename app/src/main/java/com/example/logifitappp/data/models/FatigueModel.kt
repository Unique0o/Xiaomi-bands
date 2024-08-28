package com.example.logifitappp.data.models

import java.time.LocalDateTime

data class FatigueModel(
    val id: Int,
    val createdAt: LocalDateTime,
    val reemCycles: Int? = null,
    val sentAt: String? = null,
    val totalAwakeSeconds: Int,
    val totalReemSeconds: Int? = null,
    val totalSleepSeconds: Int,
    val wearableInternalIdentifier: Int,
    val withAwakeningOvercome: Boolean,
    val withHypertension: Boolean,
    val withLittleReemSleep: Boolean? = null,
    val withLittleSleep: Boolean,
    val withLongAwake: Boolean
)
