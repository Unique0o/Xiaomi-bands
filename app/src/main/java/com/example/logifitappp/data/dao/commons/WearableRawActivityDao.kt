package com.example.logifitappp.data.dao.commons

import androidx.room.RawQuery
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.logifitappp.data.models.commons.WearableRawActivityModel

abstract class WearableRawActivityDao<T: WearableRawActivityModel>(private val tableName: String) {
    @RawQuery
    protected abstract fun getRawActivitiesBetween(query: SupportSQLiteQuery): List<T>

    @Upsert
    abstract fun store(vararg activities: T)

    fun getRawActivitiesBetween(from: Long, to: Long, wearableId: Int): List<T> {
        return getRawActivitiesBetween(
            SimpleSQLiteQuery("SELECT * FROM $tableName WHERE timestamp >= $from AND timestamp <= $to AND wearable_id = $wearableId ORDER BY timestamp ASC")
        )
    }
}