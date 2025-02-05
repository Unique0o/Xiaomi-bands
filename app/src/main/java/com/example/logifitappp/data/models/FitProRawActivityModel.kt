package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.logifitappp.data.models.commons.WearableRawActivityModel

@Entity(
    ignoredColumns = ["provider"],
    primaryKeys = ["timestamp", "wearable_id"],
    tableName = "fit_pro_raw_activities"
)
data class FitProRawActivityModel(
    @ColumnInfo(name = "active_time_minutes") var activeTimeMinutes: Int? = null,
    @ColumnInfo(name = "calories_burnt") var caloriesBurnt: Int? = null,
    @ColumnInfo(name = "distance_meters") var distanceMeters: Int? = null,
    @ColumnInfo(name = "heart_rate") override var heartRate: Int = 0,
    override var intensity: Int = 0,
    @ColumnInfo(name = "pressure_high_mm_hg") var pressureHighMmHg: Int? = null,
    @ColumnInfo(name = "pressure_low_mm_hg") var pressureLowMmHg: Int? = null,
    @ColumnInfo(name = "spo2_percent") var spo2Percent: Int? = null,
    override var steps: Int = 0,
    override var timestamp: Long = 0,
    override var type: Int = 0,
    @ColumnInfo(name = "wearable_id") override var wearableId: Int = 0
): WearableRawActivityModel(heartRate, intensity, steps, timestamp, type, wearableId)
