package com.example.logifitappp.data.dao.commons

import androidx.room.RawQuery
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.logifitappp.data.models.commons.WearableSampleModel

abstract class WearableSampleDao<T: WearableSampleModel>(private val tableName: String) {
    @RawQuery
    protected abstract fun getSamplesBetween(query: SupportSQLiteQuery): List<T>

    fun getSamplesBetween(from: Long, to: Long, wearableId: Int): List<T> {
        return getSamplesBetween(
            SimpleSQLiteQuery("SELECT * FROM $tableName WHERE timestamp >= $from AND timestamp <= $to AND wearable_id = $wearableId ORDER BY timestamp ASC")
        )
    }

    @Upsert
    abstract fun store(vararg samples: T)
}