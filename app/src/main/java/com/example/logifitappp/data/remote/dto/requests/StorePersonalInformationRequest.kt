package com.example.logifitappp.data.remote.dto.requests

data class StorePersonalInformationRequest(
    val country_id: Int? = null,
    val date_birth: String? = null,
    val departament_id: Int? = null,
    val dni: String? = null,
    val document_type_id: Int? = null,
    val email: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val phone: String? = null,
    val province_id: Int? = null
)