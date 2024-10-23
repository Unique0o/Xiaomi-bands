package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.logifitappp.data.models.ShiftModel

@Dao
abstract class ShiftDao {
    @Query("SELECT * FROM shifts WHERE tenant_id = :tenantId")
    abstract fun all(tenantId: Int): List<ShiftModel>

    @Query("DELETE FROM shifts WHERE tenant_id = :tenantId")
    abstract fun delete(tenantId: Int)

    @Query("SELECT * FROM shifts WHERE id = :id LIMIT 1")
    abstract fun find(id: Int): ShiftModel?

    @Upsert
    abstract fun store(vararg shifts: ShiftModel)
}