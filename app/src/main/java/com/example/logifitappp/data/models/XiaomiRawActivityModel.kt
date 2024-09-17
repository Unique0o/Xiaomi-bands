package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.logifitappp.data.models.commons.WearableRawActivityModel

@Entity(
    ignoredColumns = ["provider"],
    primaryKeys = ["timestamp", "wearable_id"],
    tableName = "xiaomi_raw_activities"
)
data class XiaomiRawActivityModel(
    @ColumnInfo(name = "heart_rate") override var heartRate: Int = 0,
    override var intensity: Int = 0,
    override var steps: Int = 0,
    var stress: Int? = null,
    var spo2: Int? = null,
    override var timestamp: Long = 0,
    override var type: Int = 0,
    @ColumnInfo(name = "wearable_id") override var wearableId: Int = 0
): WearableRawActivityModel(heartRate, intensity, steps, timestamp, type, wearableId)