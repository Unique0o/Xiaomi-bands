package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tenants")
data class TenantModel(
    @ColumnInfo(name = "can_share_sleep_data") val canShareSleepData: Boolean = false,
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    val name: String,
    @ColumnInfo(name = "should_it_show_drowsiness_test") val shouldItShowDrowsinessTest: Boolean = false,
    @ColumnInfo(name = "should_it_show_location_component") val shouldItShowLocationComponent: Boolean = false,
    @ColumnInfo(name = "sleep_analysis_hours") val sleepAnalysisHours: Long = 6 * 60 * 60
)