package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.logifitappp.data.models.WearableModel

@Dao
abstract class WearableDao {
    @Delete
    abstract fun delete(wearable: WearableModel)

    @Query("SELECT * FROM wearables WHERE user_id = :userId")
    abstract fun all(userId: Int): List<WearableModel>

    @Upsert
    abstract fun store(vararg wearables: WearableModel)

    @Query("SELECT * FROM wearables WHERE mac = :mac AND user_id = :userId LIMIT 1")
    abstract fun find(mac: String, userId: Int): WearableModel?
}