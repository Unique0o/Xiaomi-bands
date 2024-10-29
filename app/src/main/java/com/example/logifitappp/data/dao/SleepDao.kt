package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.logifitappp.data.models.SleepModel

@Dao
abstract class SleepDao {
    @Query("DELETE FROM sleeps WHERE wearable_id = :wearableId")
    abstract fun delete(wearableId: Int)

    @Query("DELETE FROM sleeps WHERE wearable_id = :wearableId AND created_at LIKE :date || '%'")
    abstract fun deleteFromDate(wearableId: Int, date: String)

    @Upsert
    abstract fun store(vararg sleeps: SleepModel)
}