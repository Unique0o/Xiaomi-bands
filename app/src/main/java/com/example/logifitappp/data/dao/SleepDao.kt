package com.example.logifitappp.data.dao

import android.icu.util.GregorianCalendar
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.data.models.SleepModel

@Dao
abstract class SleepDao {
    @Query("DELETE FROM sleeps WHERE wearable_id = :wearableId")
    abstract fun delete(wearableId: Int)

    @Query("DELETE FROM sleeps WHERE wearable_id = :wearableId AND created_at LIKE :date || '%'")
    abstract fun deleteFromDate(wearableId: Int, date: String)

    @Query("SELECT * FROM sleeps WHERE wearable_id = :wearableId AND created_at LIKE :date || '%'")
    abstract fun fetchFromDate(wearableId: Int, date: String): List<SleepModel>

    fun fetchFromToday(wearableId: Int): List<SleepModel> {
        return fetchFromDate(wearableId, DateTimeUtils.formatReducedIso8601(GregorianCalendar.getInstance().time))
    }

    @Upsert
    abstract fun store(vararg sleeps: SleepModel)
}