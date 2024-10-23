package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shifts")
data class ShiftModel(
    @ColumnInfo(name = "days_to_apply_sleep_time_extension") val daysToApplySleepTimeExtension: String? = null,
    @ColumnInfo(name = "end_time") val endTime: String,
    @PrimaryKey val id: Int,
    val name: String,
    @ColumnInfo(name = "sleep_time_extension_hours") val sleepTimeExtensionHours: Int? = null,
    @ColumnInfo(name = "start_time") val startTime: String,
    @ColumnInfo(name = "tenant_id") val tenantId: Int
)