package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.logifitappp.data.models.TenantModel

@Dao
abstract class TenantDao {
    @Query("SELECT * FROM tenants WHERE id = :id LIMIT 1")
    abstract fun find(id: Int): TenantModel?

    @Upsert
    abstract fun store(tenant: TenantModel)
}