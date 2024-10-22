package com.example.logifitappp.data.remote.dto.requests

data class PersonalInformationRequest (
    var country_id: Int,
    var date_birth: String?,
    var departament_id: Int?,
    var dni: String,
    var document_type_id: Int,
    var email: String?,
    var first_name: String,
    var last_name: String,
    var phone: String,
    var profile: String?,
    var province_id: Int?,
)
