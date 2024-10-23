package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sleep_conditions")
data class SleepConditionModel(
    @ColumnInfo(name = "background_color") val backgroundColor: String,
    val color: String,
    @ColumnInfo(name = "end_seconds") val endSeconds: Long,
    @PrimaryKey val id: Int,
    val name: String,
    @ColumnInfo(name = "start_seconds") val startSeconds: Long,
    @ColumnInfo(name = "tenant_id") val tenantId: Int
)
