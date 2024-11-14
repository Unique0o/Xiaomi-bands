package com.example.logifitappp.data.dao

import android.icu.util.GregorianCalendar
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.data.models.FatigueModel

@Dao
abstract class FatigueDao {
    @Query("SELECT * FROM fatigues WHERE wearable_id = :wearableId AND created_at LIKE :date || '%' LIMIT 1")
    abstract fun findFromDate(wearableId: Int, date: String): FatigueModel?

    fun findFromToday(wearableId: Int): FatigueModel? {
        return findFromDate(wearableId, DateTimeUtils.formatReducedIso8601(GregorianCalendar.getInstance().time))
    }

    @Upsert
    abstract fun store(fatigue: FatigueModel)
}