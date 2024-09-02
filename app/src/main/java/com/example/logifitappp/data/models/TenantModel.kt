package com.example.logifitappp.data.models

data class TenantModel(
    val id: Int,
    val canShareSleepData: Boolean = false,
    val externalIdentifier: Int,
    val isActive: Boolean,
    val name: String,
    val shouldItShowDrowsinessTest: Boolean = false,
    val shouldItShowLocationComponent: Boolean = false,
    val sleepAnalysisHours: Int = 6 * 60
)