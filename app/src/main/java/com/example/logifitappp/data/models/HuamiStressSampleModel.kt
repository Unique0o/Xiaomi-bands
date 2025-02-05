package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.logifitappp.data.models.commons.WearableStressSampleModel
import com.example.logifitappp.enums.StressTypeEnum

@Entity(
    primaryKeys = ["timestamp", "wearable_id"],
    tableName = "huami_stress_samples"
)
data class HuamiStressSampleModel(
    override var stress: Int = 0,
    override var timestamp: Long = 0,
    @ColumnInfo(name = "type_num") var typeNum: Int = StressTypeEnum.AUTOMATIC.num,
    @ColumnInfo(name = "wearable_id") override var wearableId: Int = 0
): WearableStressSampleModel(stress, timestamp, wearableId)