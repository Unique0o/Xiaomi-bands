package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("attention") val attention: Int?,
    @SerializedName("type_band") val bandType: String?,
    @SerializedName("date_birth") val birthDate: String?,
    @SerializedName("blood_type") val bloodType: String?,
    @SerializedName("break_after") val breakAfter: Int?,
    @SerializedName("break_average") val breakAverage: Int?,
    @SerializedName("break_frecuency") val breakFrequency: Int?,
    @SerializedName("commuting") val commuting: Int?,
    @SerializedName("continuos_work") val continuousWork: Int?,
    @SerializedName("country_id") val countryId: Int?,
    @SerializedName("departament_id") val departmentId: Int?,
    @SerializedName("dni") val dni: String?,
    @SerializedName("document_type_id") val documentTypeId: Int?,
    @SerializedName("email") val email: String?,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("function_name") val functionName: String?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("group_id") val groupId: Int?,
    @SerializedName("height") val height: Float?,
    @SerializedName("id") val id: Int,
    @SerializedName("license_user") val license: LicenseResponse?,
    @SerializedName("state") val isActive: Int,
    @SerializedName("job_name") val jobName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("location_aux_id") val locationId: Int?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("profile") val profilePhoto: String?,
    @SerializedName("province_id") val provinceId: Int?,
    @SerializedName("rol") val role: Int,
    @SerializedName("shift_id") val shiftId: Int?,
    @SerializedName("tenant_id") val tenantId: Int,
    @SerializedName("weight") val weight: Float?,
    @SerializedName("workload") val workload: Int?
)
