package com.example.logifitappp.data.remote.dto.requests

data class AssociateWearableRequest(
    val device_mac: String,
    val firmware: String? = null,
    val oper_system: String? = null,
    val oper_system_version: String? = null,
    val phone_brand: String? = null,
    val phone_model: String? = null
)