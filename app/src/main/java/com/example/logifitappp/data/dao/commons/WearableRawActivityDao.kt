package com.example.logifitappp.data.dao.commons

import androidx.room.RawQuery
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.logifitappp.data.models.commons.WearableRawActivityModel

abstract class WearableRawActivityDao<T: WearableRawActivityModel>(private val tableName: String) {
    @RawQuery
    abstract fun delete(query: SupportSQLiteQuery): Int

    fun delete(wearableId: Int) {
        delete(
            SimpleSQLiteQuery("DELETE FROM $tableName WHERE wearable_id = $wearableId")
        )
    }

    @RawQuery
    abstract fun findLastActivity(query: SupportSQLiteQuery): T?

    fun findLastActivity(wearableId: Int): T? {
        return findLastActivity(
            SimpleSQLiteQuery("SELECT * FROM $tableName WHERE wearable_id = $wearableId ORDER BY timestamp DESC LIMIT 1")
        )
    }

    @RawQuery
    protected abstract fun getRawActivitiesBetween(query: SupportSQLiteQuery): List<T>

    fun getRawActivitiesBetween(from: Long, to: Long, wearableId: Int): List<T> {
        return getRawActivitiesBetween(
            SimpleSQLiteQuery("SELECT * FROM $tableName WHERE timestamp >= $from AND timestamp <= $to AND wearable_id = $wearableId ORDER BY timestamp ASC")
        )
    }

    @Upsert
    abstract fun store(vararg activities: T)
}