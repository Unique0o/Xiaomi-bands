package com.example.logifitappp.data.remote.dto.requests

data class StoreRangeSleepRequest(
    val date: String? = null,
    val realSleepIntervalText: String? = null,
    val realSleepIntervalValue: String? = null,
    val version: String? = null
)

data class StoreSleepMetadataRequest(
    val battery: String? = null,
    val dateRegister: String? = null,
    val deepSleepText: String? = null,
    val deepSleepValue: String? = null,
    val identifier: String? = null,
    val interruptions: Int,
    val lightSleepText: String? = null,
    val lightSleepValue: String? = null,
    val location_id: String,
    val realSleepIntervalText: String? = null,
    val realSleepIntervalValue: String? = null,
    val reemSleepText: String? = null,
    val reemSleepValue: String? = null,
    val referenceAppId: Int? = null,
    val sleepIni: String? = null,
    val sleepEnd: String? = null,
    val status: Int,
    val syncDate: String,
    val totalSleepText: String? = null,
    val totalSleepValue: String? = null
)

data class StoreStepRequest(
    val caloriesValue: Double,
    val caloriesText: String,
    val distanceText: String,
    val distanceValue: Double,
    val step: Long
)

data class StoreSleepRequest(
    val real_sleeps: List<StoreRangeSleepRequest>? = null,
    val shift_id: Int,
    val sleeps: List<StoreSleepMetadataRequest>,
    val steps: List<StoreStepRequest>? = null
)
