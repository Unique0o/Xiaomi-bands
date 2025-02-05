package com.example.logifitappp.data.models

import android.icu.util.GregorianCalendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.enums.SleepProcessingStatusEnum

@Entity(tableName = "fatigues")
data class FatigueModel(
    @ColumnInfo("created_at") val createdAt: String = DateTimeUtils.formatExtendedIso8601(GregorianCalendar.getInstance().time),
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo("rem_cycles") var remCycles: Int? = null,
    @ColumnInfo("sent_at") var sentAt: String? = null,
    @ColumnInfo("total_awake_seconds") var totalAwakeSeconds: Long,
    @ColumnInfo("total_rem_seconds") var totalRemSeconds: Long? = null,
    @ColumnInfo("total_sleep_seconds") var totalSleepSeconds: Long,
    @ColumnInfo("wearable_id") val wearableId: Int,
    @ColumnInfo("with_awakening_overcome") var withAwakeningOvercome: Boolean,
    @ColumnInfo("with_hypertension") var withHypertension: Boolean = false,
    @ColumnInfo("with_little_reem_sleep") var withLittleReemSleep: Boolean? = null,
    @ColumnInfo("with_little_sleep") var withLittleSleep: Boolean,
    @ColumnInfo("with_long_awake") var withLongAwake: Boolean
) {
    fun calculateStatus(): SleepProcessingStatusEnum {
        val parameters = arrayOf(withLittleSleep, withLittleReemSleep, withLongAwake, withHypertension, withAwakeningOvercome)
        val parametersExceeded = parameters.filter { it == true }

        return when (parametersExceeded.size) {
            0 -> SleepProcessingStatusEnum.SUITABLE
            1 -> SleepProcessingStatusEnum.WITH_OBSERVATIONS
            else -> SleepProcessingStatusEnum.UNSUITABLE
        }
    }
}
