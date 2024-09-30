package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class Condition(
    @SerializedName("label") val label: String,
    @SerializedName("valor") val value: String
)

data class Fatigue(
    @SerializedName("reem_cycles") val reemCycles: Int? = null,
    @SerializedName("total_awake_time") val totalAwakeTime: String,
    @SerializedName("total_reem_sleep") val totalReemSleep: String? = null,
    @SerializedName("total_sleep") val totalSleep: String,
    @SerializedName("with_awakening_overcome")  val withAwakeningOvercome: Boolean,
    @SerializedName("with_hypertension")  val withHypertension: Boolean,
    @SerializedName("with_little_reem_sleep")  val withLittleReemSleep: Boolean,
    @SerializedName("with_little_sleep") val withLittleSleep: Boolean,
    @SerializedName("with_long_awake")  val withLongAwake: Boolean
)

data class Report(
    @SerializedName("background_color") val backgroundColor: String = "#fadab1",
    @SerializedName("color") val color: String = "#ffab40",
    @SerializedName("condicion") val condition: String,
    @SerializedName("fatigue") val fatigue: Fatigue? = null,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("grupo") val group: String? = null,
    @SerializedName("group_id") val groupId: Int? = null,
    @SerializedName("turno") val shift: String,
    @SerializedName("id_turno") val shiftId: Int
)

data class Group(
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: Int
)

data class Shift(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)


data class UnsynchronizedUser(
    @SerializedName("full_name") val fullName: String,
    val group: Group? = null,
    val id: Int,
    val shift: Shift? = null
)

data class SleepReportResponse(
    @SerializedName("count_condiciones") val conditions: List<Condition>,
    @SerializedName("data") val sleepData: List<Report>,
    @SerializedName("sin_datos") val unsynchronizedUsers: List<UnsynchronizedUser>
)