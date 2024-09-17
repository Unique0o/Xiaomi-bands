package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.logifitappp.data.models.commons.WearableTimeModel

@Entity(
    primaryKeys = ["timestamp", "wearable_id"],
    tableName = "xiaomi_sleep_stages"
)
data class XiaomiSleepStageModel(
    var stage: Int? = null,
    override var timestamp: Long = 0,
    @ColumnInfo(name = "wearable_id") override var wearableId: Int = 0
): WearableTimeModel(timestamp, wearableId)