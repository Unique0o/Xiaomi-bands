package com.example.logifitappp.data.dao

import android.icu.util.GregorianCalendar
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.data.models.DrowsinessModel

@Dao
abstract class DrowsinessDao {
    @Query("SELECT * FROM drowsiness WHERE wearable_id = :wearableId AND created_at LIKE :date || '%' LIMIT 1")
    abstract fun findFromDate(wearableId: Int, date: String): DrowsinessModel?

    fun findFromToday(wearableId: Int): DrowsinessModel? {
        return findFromDate(wearableId, DateTimeUtils.formatReducedIso8601(GregorianCalendar.getInstance().time))
    }

    @Upsert
    abstract fun store(drowsiness: DrowsinessModel)
}