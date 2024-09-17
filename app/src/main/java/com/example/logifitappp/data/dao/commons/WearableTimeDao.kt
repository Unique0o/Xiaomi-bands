package com.example.logifitappp.data.dao.commons

import androidx.room.RawQuery
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.logifitappp.data.models.commons.WearableTimeModel

abstract class WearableTimeDao<T: WearableTimeModel>(private val tableName: String) {
    @RawQuery
    protected abstract fun getBetween(query: SupportSQLiteQuery): List<T>

    @RawQuery
    abstract fun getLastBeforeOf(query: SupportSQLiteQuery): T?

    @Upsert
    abstract fun store(vararg activities: T)

    fun getBetween(from: Long, to: Long, wearableId: Int): List<T> {
        return getBetween(
            SimpleSQLiteQuery("SELECT * FROM $tableName WHERE timestamp >= $from AND timestamp <= $to AND wearable_id = $wearableId")
        )
    }

    fun getLastBeforeOf(timestamp: Long, wearableId: Int): T? {
        return getLastBeforeOf(
            SimpleSQLiteQuery("SELECT * FROM xiaomi_sleep_times WHERE timestamp <= $timestamp AND wearable_id = $wearableId ORDER BY timestamp DESC LIMIT 1")
        )
    }
}