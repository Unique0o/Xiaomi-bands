package com.example.logifitappp.data.remote.dto.requests

data class StoreSleepByMinuteRequest(
    val heartRate: Int,
    val intensity: Double,
    val rawKind: Int,
    val steps: Int,
    val timestamp: String
)

data class StoreSleepGraphicMetadataRequest(
    val dateEnd: String,
    val dateStart: String,
    val detail: List<StoreSleepByMinuteRequest>,
    val firmware: String? = null,
    val identifier: String,
    val oper_system: String? = null,
    val oper_system_version: String? = null,
    val phone_brand: String? = null,
    val phone_model: String? = null
)

data class StoreSleepGraphicRequest(
    val sleep_graphics: StoreSleepGraphicMetadataRequest
)
