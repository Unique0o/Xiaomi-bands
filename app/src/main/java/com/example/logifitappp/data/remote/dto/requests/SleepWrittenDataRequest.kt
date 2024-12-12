package com.example.logifitappp.data.remote.dto.requests

import com.google.gson.annotations.SerializedName

data class SleepWrittenDataRequest(
    @SerializedName("real_sleep") val realSleep: RealSleep,
    @SerializedName("sleeps") val sleeps: List<Sleep>,
    @SerializedName("shift_id") val shiftId: Int? = null,
    @SerializedName("user_id") val userId: Int? = null,
    @SerializedName("evidence") val evidence: String
)

data class RealSleep(
    @SerializedName("intervalText") val intervalText: String,
    @SerializedName("intervalValue") val intervalValue: String,
    @SerializedName("date") val date: String,
    @SerializedName("version") val version: String = "4.6"
)

data class Sleep(
    @SerializedName("sleepIni") val sleepIni: String,
    @SerializedName("sleepEnd") val sleepEnd: String,
    @SerializedName("totalSleepText") val totalSleepText: String,
    @SerializedName("totalSleepValue") val totalSleepValue: String
)