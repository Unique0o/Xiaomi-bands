package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    indices = [Index(value = ["mac", "user_id"], unique = true)],
    tableName = "wearables"
)
data class WearableModel(
    var alias: String? = null,
    @ColumnInfo(name = "firmware_version") var firmwareVersion: String? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var mac: String = "",
    var name: String = "",
    @ColumnInfo(name = "shift_id") var shiftId: Int? = null,
    @ColumnInfo(name = "type_name") var typeName: String = "",
    @ColumnInfo(name = "user_id") var userId: Int = 0
)