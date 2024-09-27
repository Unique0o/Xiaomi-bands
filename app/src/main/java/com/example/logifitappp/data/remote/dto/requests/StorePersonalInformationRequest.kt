package com.example.logifitappp.data.remote.dto.requests

data class StorePersonalInformationRequest(
    val country_id: Int?,
    val date_birth: String?,
    val departament_id: Int?,
    val dni: String?,
    val document_type_id: Int?,
    val email: String?,
    val first_name: String?,
    val last_name: String?,
    val phone: String?,
    val profile: String?,
    val province_id: Int?
)