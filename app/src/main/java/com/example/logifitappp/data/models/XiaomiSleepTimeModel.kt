package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.logifitappp.data.models.commons.WearableTimeModel

@Entity(
    primaryKeys = ["timestamp", "wearable_id"],
    tableName = "xiaomi_sleep_times"
)
data class XiaomiSleepTimeModel(
    @ColumnInfo(name = "awake_duration") var awakeDuration: Int? = null,
    @ColumnInfo(name = "deep_sleep_duration") var deepSleepDuration: Int? = null,
    @ColumnInfo(name = "is_awake") var isAwake: Boolean = false,
    @ColumnInfo(name = "light_sleep_duration") var lightSleepDuration: Int? = null,
    @ColumnInfo(name = "rem_sleep_duration") var remSleepDuration: Int? = null,
    override var timestamp: Long = 0,
    @ColumnInfo(name = "total_duration") var totalDuration: Int? = null,
    @ColumnInfo(name = "wakeup_time") var wakeupTime: Long? = null,
    @ColumnInfo(name = "wearable_id") override var wearableId: Int = 0
): WearableTimeModel(timestamp, wearableId)