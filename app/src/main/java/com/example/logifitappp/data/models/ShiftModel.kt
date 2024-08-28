package com.example.logifitappp.data.models

data class ShiftModel(
    val id: Int,
    val daysToApplySleepTimeExtension: String? = null,
    val endTime: String,
    val externalIdentifier: Int,
    val name: String,
    val sleepTimeExtensionHours: Int? = null,
    val startTime: String,
    val tenantExternalIdentifier: Int
)