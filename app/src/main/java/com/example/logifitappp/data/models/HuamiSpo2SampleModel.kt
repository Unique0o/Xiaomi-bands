package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.logifitappp.data.models.commons.WearableSpo2SampleModel
import com.example.logifitappp.enums.Spo2ModeEnum

@Entity(
    primaryKeys = ["timestamp", "wearable_id"],
    tableName = "huami_spo2_samples"
)
data class HuamiSpo2SampleModel(
    override var modeName: String = Spo2ModeEnum.AWAKE.name,
    override var spo2: Int = 0,
    override var timestamp: Long = 0,
    @ColumnInfo(name = "type_num") var typeNum: Int = 0,
    @ColumnInfo(name = "wearable_id") override var wearableId: Int = 0
): WearableSpo2SampleModel(modeName, spo2, timestamp, wearableId)
