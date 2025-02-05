package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.logifitappp.data.models.ShiftModel

@Dao
abstract class ShiftDao {
    @Query("SELECT * FROM shifts WHERE tenant_id = :tenantId")
    abstract fun all(tenantId: Int): List<ShiftModel>

    @Query("DELETE FROM shifts WHERE tenant_id = :tenantId")
    abstract fun delete(tenantId: Int)

    @Query("SELECT * FROM shifts WHERE id = :id AND tenant_id = :tenantId LIMIT 1")
    abstract fun find(id: Int, tenantId: Int): ShiftModel?

    @Transaction
    open suspend fun replaceAll(tenantId: Int, vararg shifts: ShiftModel) {
        delete(tenantId)
        store(*shifts)
    }

    @Upsert
    abstract fun store(vararg shifts: ShiftModel)
}