package com.example.logifitappp.data.models

import android.icu.util.GregorianCalendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.logifitappp.core.utils.DateTimeUtils

@Entity(tableName = "sleeps")
data class SleepModel(
    @ColumnInfo("created_at") val createdAt: String = DateTimeUtils.formatExtendedIso8601(GregorianCalendar.getInstance().time),
    @ColumnInfo("deep_sleep_seconds") val deepSleepSeconds: Int,
    @ColumnInfo("end_at") val endAt: String,
    @PrimaryKey(autoGenerate = true) val id: Long,
    val interruptions: Int,
    @ColumnInfo("light_sleep_seconds") val lightSleepSeconds: Int,
    @ColumnInfo("reem_sleep_seconds") val reemSleepSeconds: Int,
    @ColumnInfo("start_at") val startAt: String,
    @ColumnInfo("total_sleep_seconds") val totalSleepSeconds: Int,
    @ColumnInfo("wearable_id") val wearableId: Int
)