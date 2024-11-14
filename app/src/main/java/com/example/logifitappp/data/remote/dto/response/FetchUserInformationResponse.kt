package com.example.logifitappp.data.remote.dto.response

import com.example.logifitappp.data.models.UserModel
import com.google.gson.annotations.SerializedName

data class FetchUserInformationResponse(
    @SerializedName("data") val data: UserResponse
)

fun FetchUserInformationResponse.toUser(accessToken: String) = UserModel(
    accessToken = accessToken,
    attentionValue = data.attention,
    bandType = data.bandType ?: "MIBAND",
    birthDate = data.birthDate,
    bloodType = data.bloodType,
    breakAfterSeconds = data.breakAfter,
    breakAverageSeconds = data.breakAverage,
    breakFrequencySeconds = data.breakFrequency,
    commutingSeconds = data.commuting,
    continuousWorkSeconds = data.continuousWork,
    countryId = data.countryId,
    departmentId = data.departmentId,
    documentId = data.documentTypeId,
    email = data.email,
    firstName = data.firstName,
    functionName = data.functionName,
    groupId = data.groupId,
    hasLoggedIn = true,
    height = data.height,
    id = data.id,
    identificationDocument = data.dni,
    isActive = data.isActive == 1,
    lastName = data.lastName,
    license = data.license?.licenseType,
    locationId = data.locationId,
    phone = data.phone,
    profilePhoto = data.profilePhoto,
    provinceId = data.provinceId,
    role = data.role,
    shiftId = data.shiftId,
    tenantId = data. tenantId,
    weight = data.weight,
    workPosition = data.jobName,
    workloadValue = data. workload
)