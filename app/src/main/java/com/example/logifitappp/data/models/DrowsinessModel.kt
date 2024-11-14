package com.example.logifitappp.data.models

import android.icu.util.GregorianCalendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.logifitappp.core.utils.DateTimeUtils

@Entity(tableName = "drowsiness")
data class DrowsinessModel(
    @ColumnInfo("created_at") val createdAt: String = DateTimeUtils.formatExtendedIso8601(GregorianCalendar.getInstance().time),
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("sent_at") val sentAt: String? = null,
    @ColumnInfo("total_sleep_seconds") var totalSleepSeconds: Long,
    @ColumnInfo("wearable_id") val wearableId: Int
)