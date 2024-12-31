package com.example.logifitappp.data.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sick
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.ColorUtils
import com.example.logifitappp.enums.ChipStatusEnum
import com.example.logifitappp.enums.SleepProcessingStatusEnum
import com.example.logifitappp.ui.theme.Orange390

@Entity(tableName = "sleep_conditions")
data class SleepConditionModel(
    @ColumnInfo(name = "background_color") val backgroundColor: String,
    val color: String,
    @ColumnInfo(name = "end_seconds") val endSeconds: Long,
    @PrimaryKey val id: Int,
    val name: String,
    @ColumnInfo(name = "start_seconds") val startSeconds: Long,
    @ColumnInfo(name = "tenant_id") val tenantId: Int
) {
    fun calculateStatus() = when (name.lowercase()) {
        "apto" -> SleepProcessingStatusEnum.SUITABLE
        "no apto" -> SleepProcessingStatusEnum.UNSUITABLE
        else -> SleepProcessingStatusEnum.CUSTOM(
            ChipStatusEnum.CUSTOM(ColorUtils.toColor(color), ColorUtils.toColor(backgroundColor)),
            Orange390, Icons.Default.Sick, R.string.fit_with_observations
        )
    }
}
