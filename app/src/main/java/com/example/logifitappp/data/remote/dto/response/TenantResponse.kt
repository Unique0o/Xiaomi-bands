package com.example.logifitappp.data.remote.dto.response

import com.example.logifitappp.data.models.GroupModel
import com.example.logifitappp.data.models.LocationModel
import com.example.logifitappp.data.models.TenantModel
import com.google.gson.annotations.SerializedName

data class Tenant(
    @SerializedName("groups") val groups: List<InformationResponse>,
    @SerializedName("id") val id: Int,
    @SerializedName("state") val isActive: Int,
    @SerializedName("locations_aux") val locations: List<InformationResponse>,
    @SerializedName("name") val name: String,
    @SerializedName("send_whatsapp") val sendWhatsApp: Boolean,
    @SerializedName("shifts") val shifts: List<ShiftResponse>,
    @SerializedName("test") val shouldItShowDrowsinessTests: Int,
    @SerializedName("localization") val shouldItShowLocationComponent: Int,
    @SerializedName("sleep_conditions") val sleepConditions: List<SleepConditionResponse>,
    @SerializedName("time_analysis") val timeAnalysis: Long?,
)

data class TenantResponse(
    @SerializedName("data") val data: Tenant
)

fun TenantResponse.getGroupModels() = data.groups.map {
    GroupModel(
        id = it.id,
        name = it.name,
        tenantId = data.id
    )
}

fun TenantResponse.getLocationModels() = data.locations.map {
    LocationModel(
        id = it.id,
        name = it.name,
        tenantId = data.id
    )
}

fun TenantResponse.getShiftModels() = data.shifts.map { it.toShiftModel() }

fun TenantResponse.getSleepConditionModels() = data.sleepConditions.map { it.toSleepConditionModel() }

fun TenantResponse.toTenantModel() = TenantModel(
    canShareSleepData = data.sendWhatsApp,
    id = data.id,
    isActive = data.isActive == 1,
    name = data.name,
    shouldItShowDrowsinessTest = data.shouldItShowDrowsinessTests == 1,
    shouldItShowLocationComponent = data.shouldItShowLocationComponent == 1,
    sleepAnalysisHours = data.timeAnalysis ?: (6 * 60 * 60)
)
