package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.logifitappp.data.models.LocationModel

@Dao
abstract class LocationDao {
    @Query("SELECT * FROM locations WHERE tenant_id = :tenantId")
    abstract fun all(tenantId: Int): List<LocationModel>

    @Query("DELETE FROM locations WHERE tenant_id = :tenantId")
    abstract fun delete(tenantId: Int)

    @Query("SELECT * FROM locations WHERE id = :id LIMIT 1")
    abstract fun find(id: Int): LocationModel?

    @Transaction
    open suspend fun replaceAll(tenantId: Int, vararg locations: LocationModel) {
        delete(tenantId)
        store(*locations)
    }

    @Upsert
    abstract fun store(vararg locations: LocationModel)
}