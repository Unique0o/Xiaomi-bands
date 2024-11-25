package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.logifitappp.data.models.RosterLocationModel

@Dao
abstract class RosterLocationDao {
    @Query("SELECT * FROM roster_locations WHERE tenant_id = :tenantId")
    abstract fun all(tenantId: Int): List<RosterLocationModel>

    @Query("DELETE FROM roster_locations WHERE tenant_id = :tenantId")
    abstract fun delete(tenantId: Int)

    @Upsert
    abstract fun store(vararg locations: RosterLocationModel)
}