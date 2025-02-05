package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.logifitappp.data.models.SleepConditionModel

@Dao
abstract class SleepConditionDao {
    @Query("DELETE FROM sleep_conditions WHERE tenant_id = :tenantId")
    abstract fun delete(tenantId: Int)

    @Query("SELECT * FROM sleep_conditions WHERE tenant_id = :tenantId AND start_seconds <= :seconds AND end_seconds > :seconds LIMIT 1")
    abstract fun findAppropriate(seconds: Long, tenantId: Int): SleepConditionModel?

    @Transaction
    open suspend fun replaceAll(tenantId: Int, vararg sleepConditions: SleepConditionModel) {
        delete(tenantId)
        store(*sleepConditions)
    }

    @Upsert
    abstract fun store(vararg sleepConditions: SleepConditionModel)
}