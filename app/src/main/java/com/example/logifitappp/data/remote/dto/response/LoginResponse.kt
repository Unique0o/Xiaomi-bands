package com.example.logifitappp.data.remote.dto.response

import com.example.logifitappp.data.models.UserModel
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("user") val user: UserResponse
)

fun LoginResponse.toUser() = UserModel(
    accessToken = accessToken,
    attentionValue = user.attention,
    bandType = user.bandType ?: "MIBAND",
    birthDate = user.birthDate,
    bloodType = user.bloodType,
    breakAfterSeconds = user.breakAfter,
    breakAverageSeconds = user.breakAverage,
    breakFrequencySeconds = user.breakFrequency,
    commutingSeconds = user.commuting,
    continuousWorkSeconds = user.continuousWork,
    countryId = user.countryId,
    departmentId = user.departmentId,
    documentId = user.documentTypeId,
    email = user.email,
    firstName = user.firstName,
    functionName = user.functionName,
    groupId = user.groupId,
    hasLoggedIn = true,
    height = user.height,
    id = user.id,
    identificationDocument = user.dni,
    isActive = user.isActive == 1,
    lastName = user.lastName,
    license = user.license?.licenseType,
    locationId = user.locationId,
    phone = user.phone,
    profilePhoto = user.profilePhoto,
    provinceId = user.provinceId,
    role = user.role,
    shiftId = user.shiftId,
    tenantId =  user.tenantId,
    weight = user.weight,
    workPosition = user.jobName,
    workloadValue =  user.workload
)
