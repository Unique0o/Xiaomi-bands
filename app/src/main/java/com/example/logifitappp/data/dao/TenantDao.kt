package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.logifitappp.data.models.TenantModel

@Dao
abstract class TenantDao {
    @Query("SELECT * FROM tenants WHERE id = :id LIMIT 1")
    abstract fun find(id: Int): TenantModel?

    @Query("UPDATE tenants SET is_active = 0 WHERE id = :tenantId")
    abstract fun inactive(tenantId: Int)

    @Upsert
    abstract fun store(tenant: TenantModel)
}