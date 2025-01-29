package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class ConditionResponse(
    val label: String,
    @SerializedName("valor") val value: Int
)

data class FatigueResponse(
    @SerializedName("reem_cycles") val reemCycles: Int?,
    @SerializedName("total_awake_time") val totalAwakeTime: String,
    @SerializedName("total_reem_sleep") val totalReemSleep: String?,
    @SerializedName("total_sleep") val totalSleep: String,
    @SerializedName("with_awakening_overcome")  val withAwakeningOvercome: Int,
    @SerializedName("with_hypertension")  val withHypertension: Int,
    @SerializedName("with_little_reem_sleep")  val withLittleReemSleep: Int,
    @SerializedName("with_little_sleep") val withLittleSleep: Int,
    @SerializedName("with_long_awake")  val withLongAwake: Int
)

data class ReportResponse(
    @SerializedName("background_color") val backgroundColor: String?,
    val color: String?,
    @SerializedName("condicion") val condition: String,
    val fatigue: FatigueResponse? = null,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("grupo") val group: String?,
    @SerializedName("group_id") val groupId: Int?,
    @SerializedName("turno") val shift: String,
    @SerializedName("id_turno") val shiftId: Int,
    @SerializedName("horas_total_dormidas") val sleepTimeText: String
)

data class UnsynchronizedUserResponse(
    @SerializedName("full_name") val fullName: String,
    val group: InformationResponse?,
    val id: Int,
    val shift: InformationResponse?
)

data class SynchronizationReportResponse(
    @SerializedName("count_condiciones") val conditions: List<ConditionResponse>,
    @SerializedName("data") val sleeps: List<ReportResponse>,
    @SerializedName("sin_datos") val unsynchronizedUsers: List<UnsynchronizedUserResponse>
)