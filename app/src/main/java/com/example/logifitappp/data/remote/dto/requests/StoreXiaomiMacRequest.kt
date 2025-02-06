package com.example.logifitappp.data.remote.dto.requests

data class XiaomiMac(
    val MAC: String,
    val tenant_id: Int?,
    val TOKEN: String,
    val type: String?
)

data class StoreXiaomiMacRequest(
    val devices: List<XiaomiMac>
)
